package edu.du.campusflow.service;

import edu.du.campusflow.dto.SimpleMemberDTO;
import edu.du.campusflow.entity.ChangeHistory;
import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.Member;
import edu.du.campusflow.repository.ChangeHistoryRepository;
import edu.du.campusflow.repository.CommonCodeRepository;
import edu.du.campusflow.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChangeHistoryService {
    private final ChangeHistoryRepository changeHistoryRepository;
    private final CommonCodeRepository commonCodeRepository;
    private final MemberRepository memberRepository;


    public List<ChangeHistory> findAll() {
        return changeHistoryRepository.findAll();
    }


    // 퇴학, 제적 또는 졸업 처리
    @Transactional
    public void processExpulsionOrWithdrawalOrGraduation(Long memberId, String statusType) {
        // 1. Member 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        // 2. member_type이 101인지 확인
        if (member.getMemberType().getCodeId() != 101L) {
            throw new IllegalStateException("Only members with type 101 can be processed.");
        }

        // 3. 상태 코드 설정
        Long statusCodeId;
        switch (statusType) {
            case "expulsion":
                statusCodeId = 5L; // 퇴학 코드
                break;
            case "withdrawal":
                statusCodeId = 4L; // 제적 코드
                break;
            case "graduation":
                statusCodeId = 3L; // 졸업 코드
                break;
            default:
                throw new IllegalArgumentException("Invalid status type");
        }

        CommonCode statusCode = commonCodeRepository.findById(statusCodeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid status code"));

        // 4. Member 상태 변경
        CommonCode currentStatus = member.getAcademicStatus(); // 기존 학적 상태
        member.setAcademicStatus(statusCode); // 변경된 상태로 업데이트
        memberRepository.save(member);

        // 5. ChangeHistory 생성 및 저장
        ChangeHistory changeHistory = ChangeHistory.builder()
                .member(member)
                .beforeCode(currentStatus) // 기존 학적 상태
                .afterCode(statusCode) // 변경된 학적 상태
                .approvalDate(LocalDate.now()) // 처리 일자
                .grade(member.getGrade()) // 학년 정보
                .build();

        changeHistoryRepository.save(changeHistory);
    }


    public List<Member> getMembersWithType101() {
        return memberRepository.findAll()
                .stream()
                .filter(member -> member.getMemberType().getCodeId() == 101L) // member_type이 101L인지 확인
                .collect(Collectors.toList());
    }

    public List<SimpleMemberDTO> getMembersByType() {
        // 원하는 memberType을 지정해서 필터링
        List<Member> members = changeHistoryRepository.findMembersByType(101L); // 예: 101인 경우
        return members.stream()
                .map(SimpleMemberDTO::new)  // Member -> SimpleMemberDTO로 변환
                .collect(Collectors.toList());
    }

}
