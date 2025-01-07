package edu.du.campusflow.service;

import edu.du.campusflow.dto.ChangeRequestDto;
import edu.du.campusflow.entity.ChangeHistory;
import edu.du.campusflow.entity.ChangeRequest;
import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.Member;
import edu.du.campusflow.repository.ChangeHistoryRepository;
import edu.du.campusflow.repository.ChangeRequestRepository;
import edu.du.campusflow.repository.CommonCodeRepository;
import edu.du.campusflow.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChangeRequestService {

    private final CommonCodeRepository commonCodeRepository;  // CommonCode 테이블을 조회할 리포지토리
    private final MemberRepository memberRepository;         // Member 테이블을 조회할 리포지토리
    private final ChangeHistoryRepository changeHistoryRepository;
    private final ChangeRequestRepository changeRequestRepository;// ChangeHistory 테이블을 조회하고 저장할 리포지토리

    @Transactional
    public void processChangeRequest(ChangeRequestDto dto) {
        // Member 조회
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found with ID: " + dto.getMemberId()));

        // ChangeRequest 엔티티 생성
        ChangeRequest changeRequest = new ChangeRequest();
        changeRequest.setMember(member);

        // 기존 로직 활용
        processChangeRequest(changeRequest, dto.getNewStatusCodeId());
    }

    @Transactional
    public void processChangeRequest(ChangeRequest changeRequest,Long codeId) {


        System.out.println("CodeId: " + codeId);
        if (changeRequest == null || changeRequest.getMember() == null || changeRequest.getMember().getAcademicStatus() == null) {
            throw new IllegalArgumentException("Invalid changeRequest or member information");
        }

        // 1. ChangeRequest 저장 전에 beforeCode 자동 설정 (현재 상태)
        CommonCode beforeCode = commonCodeRepository.findById(changeRequest.getMember().getAcademicStatus().getCodeId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid current status code ID: " + changeRequest.getMember().getAcademicStatus()));

        changeRequest.setBeforeCode(beforeCode);

        // 2. codeName으로 afterCode 및 academicStatus 설정
        CommonCode afterCode = commonCodeRepository.findById(codeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid code name: " + codeId));

        changeRequest.setAfterCode(afterCode);

        // 3. 학적 상태(academicStatus) 설정: afterCode와 동일
        changeRequest.setAcademicStatus(afterCode);

        // 4. 신청 상태(applicationStatus) 설정: 신청은 '대기' 상태로 설정
        CommonCode applicationStatus = commonCodeRepository.findById(14L)  // '대기' 상태 코드 ID
                .orElseThrow(() -> new IllegalArgumentException("Invalid application status code ID"));
        changeRequest.setApplicationStatus(applicationStatus);

        // 5. 신청 날짜 설정
        changeRequest.setRequestDate(LocalDateTime.now());

        // 6. ChangeRequest 저장
        ChangeRequest savedRequest = changeRequestRepository.save(changeRequest);



        // 8. ChangeHistory 생성 및 저장
        ChangeHistory changeHistory = ChangeHistory.builder()
                .member(savedRequest.getMember())
                .beforeCode(savedRequest.getBeforeCode())
                .afterCode(savedRequest.getAfterCode())
                .approvalDate(LocalDateTime.now())
                .grade(savedRequest.getMember().getGrade())
                .build();

        changeHistoryRepository.save(changeHistory);
    }


    // 로그인한 사용자의 변동 신청 내역 조회
    public List<ChangeRequest> getChangeRequestsByMemberId(Long memberId) {
        return changeRequestRepository.findByMember_MemberId(memberId);
    }

    public List<ChangeRequest> getALlChangeRequests() {
        return changeRequestRepository.findAll();
    }

    @Transactional
    public void handleChangeRequest(Long applicationId, boolean approve) {
        // 1. 신청서 조회
        ChangeRequest changeRequest = changeRequestRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid change request ID"));

        // 2. 승인 또는 거절 상태 코드 설정
        CommonCode statusCode;
        if (approve) {
            statusCode = commonCodeRepository.findById(12L)  // 'APPROVED' 상태 코드
                    .orElseThrow(() -> new IllegalArgumentException("Invalid approval status code"));
        } else {
            statusCode = commonCodeRepository.findById(13L)  // 'REJECTED' 상태 코드 (예: 13L)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid rejection status code"));
        }

        // 3. 신청 상태 변경
        changeRequest.setApplicationStatus(statusCode);  // 신청 상태를 승인 또는 거절로 변경

        // 4. 승인인 경우 Member의 학적 상태 변경
        if (approve) {
            Member member = changeRequest.getMember();
            member.setAcademicStatus(changeRequest.getAfterCode());  // 변경 후 학적 상태로 업데이트

            // 5. Member와 ChangeRequest 저장
            memberRepository.save(member);  // Member 엔티티 저장
        }

        changeRequestRepository.save(changeRequest);  // ChangeRequest 업데이트

        // 6. ChangeHistory 생성 및 저장
        ChangeHistory changeHistory = ChangeHistory.builder()
                .member(changeRequest.getMember())
                .beforeCode(changeRequest.getBeforeCode())  // 기존 학적 상태
                .afterCode(changeRequest.getAfterCode())    // 변경된 학적 상태
                .approvalDate(LocalDateTime.now())          // 승인 또는 거절 일자
                .grade(changeRequest.getMember().getGrade()) // 학년 정보
                .build();

        changeHistoryRepository.save(changeHistory);  // 내역 저장
    }


    @Transactional
    public void deleteChangeRequest(Long applicationId,Long memberId) {
        // 1. 신청서 조회
        ChangeRequest changeRequest = changeRequestRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid change request ID"));

        // 2. 신청서에 해당하는 모든 ChangeHistory 삭제
        List<ChangeHistory> changeHistories = changeHistoryRepository.findByMember_MemberId(memberId);;
        changeHistoryRepository.deleteAll(changeHistories);

        // 3. 신청서 삭제
        changeRequestRepository.delete(changeRequest);  // 신청서 삭제
    }
}
