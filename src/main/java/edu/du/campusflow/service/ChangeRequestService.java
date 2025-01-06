package edu.du.campusflow.service;

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
    public void processChangeRequest(ChangeRequest changeRequest, Long gradeCodeId, Long newStatusCodeId) {

        // 1. ChangeRequest 저장 전에 beforeCode, afterCode 자동 설정
        // 변경 전 상태는 현재 상태(예: '재학')
        CommonCode beforeCode = commonCodeRepository.findById(changeRequest.getMember().getAcademicStatus().getCodeId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid current status code ID: " + changeRequest.getMember().getAcademicStatus()));

        // 변경 후 상태는 사용자가 선택한 새로운 상태(예: '휴학')
        CommonCode afterCode = commonCodeRepository.findById(newStatusCodeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid new status code ID: " + newStatusCodeId));

        // 2. ChangeRequest 객체에 beforeCode와 afterCode 자동 설정
        changeRequest.setBeforeCode(beforeCode);
        changeRequest.setAfterCode(afterCode);
        changeRequest.setRequestDate(LocalDateTime.now());

        // 3. 학적 상태(academicStatus) 설정: 현재 상태에서 변경 후 상태로
        CommonCode academicStatus = commonCodeRepository.findById(newStatusCodeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid academic status code ID: " + newStatusCodeId));
        changeRequest.setAcademicStatus(academicStatus);  // 학적 상태 설정

        // 4. 신청 상태(applicationStatus) 설정: 신청은 '대기' 상태로 설정
        CommonCode applicationStatus = commonCodeRepository.findById(14L)  // 대기 상태 코드 ID가 있다고 가정
                .orElseThrow(() -> new IllegalArgumentException("Invalid application status code ID"));
        changeRequest.setApplicationStatus(applicationStatus);  // 신청 상태를 대기로 설정

        // 5. ChangeRequest 저장
        ChangeRequest savedRequest = changeRequestRepository.save(changeRequest);

        // 6. CommonCode에서 학년 정보 조회
        CommonCode grade = commonCodeRepository.findById(gradeCodeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid grade code ID: " + gradeCodeId));

        // 7. ChangeHistory 생성 및 저장
        ChangeHistory changeHistory = ChangeHistory.builder()
                .member(savedRequest.getMember())
                .beforeCode(savedRequest.getBeforeCode())  // 자동 설정된 beforeCode 사용
                .afterCode(savedRequest.getAfterCode())    // 자동 설정된 afterCode 사용
                .approvalDate(LocalDateTime.now())
                .grade(grade)  // 학년 정보 설정
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
    public void approveChangeRequest(Long applicationId) {
        // 1. 신청서 조회
        ChangeRequest changeRequest = changeRequestRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid change request ID"));

        // 2. 신청 상태를 '승인' 상태로 변경
        CommonCode approvedStatus = commonCodeRepository.findById(12L)  // 'APPROVED' 값은 승인 상태 코드라고 가정
                .orElseThrow(() -> new IllegalArgumentException("Invalid approval status code"));

        changeRequest.setApplicationStatus(approvedStatus);  // 신청 상태를 승인으로 변경

        // 3. ChangeRequest 업데이트
        changeRequestRepository.save(changeRequest);

        // 4. ChangeHistory 생성 및 저장
        ChangeHistory changeHistory = ChangeHistory.builder()
                .member(changeRequest.getMember())
                .beforeCode(changeRequest.getBeforeCode())
                .afterCode(changeRequest.getAfterCode())
                .approvalDate(LocalDateTime.now())  // 승인 일자 설정
                .grade(changeRequest.getAcademicStatus())  // 학년 정보 설정
                .build();

        changeHistoryRepository.save(changeHistory);  // 승인 내역을 저장
    }
}
