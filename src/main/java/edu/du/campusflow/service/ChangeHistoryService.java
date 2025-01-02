package edu.du.campusflow.service;

import edu.du.campusflow.entity.ChangeHistory;
import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.Member;
import edu.du.campusflow.repository.ChangeHistoryRepository;
import edu.du.campusflow.repository.CommonCodeRepository;
import edu.du.campusflow.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChangeHistoryService {
    private final ChangeHistoryRepository changeHistoryRepository;
    private final MemberRepository memberRepository;


    public void saveChangeHistory(Member member, CommonCode beforeCode, CommonCode afterCode, int grade) {
        ChangeHistory changeHistory = ChangeHistory.builder()
                .member(member)
                .beforeCode(beforeCode)
                .afterCode(afterCode)
                .approvalDate(LocalDateTime.now()) // 기존 approvalDate 활용
                .grade(grade)
                .build();

        changeHistoryRepository.save(changeHistory);
    }

    /**
     * 특정 멤버의 변동 이력 조회.
     *
     * @param memberId 멤버 ID
     * @return 변동 이력 목록
     */
    public List<ChangeHistory> getChangeHistoryByMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("멤버를 찾을 수 없습니다: " + memberId));
        return changeHistoryRepository.findByMember(member);
    }


}
