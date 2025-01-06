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

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChangeRequestService {

    private final CommonCodeRepository commonCodeRepository;  // CommonCode 테이블을 조회할 리포지토리
    private final MemberRepository memberRepository;         // Member 테이블을 조회할 리포지토리
    private final ChangeHistoryRepository changeHistoryRepository;
    private final ChangeRequestRepository changeRequestRepository;// ChangeHistory 테이블을 조회하고 저장할 리포지토리

    // 변동 신청 처리
    public void requestChange(Long memberId, Long beforeCodeId, Long afterCodeId, CommonCode grade) {
        // 1. 학생 정보를 가져오기
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("학생 정보 없음"));

        // 2. 변경 전, 변경 후 코드 정보 가져오기
        CommonCode beforeCode = commonCodeRepository.findById(beforeCodeId).orElseThrow(() -> new IllegalArgumentException("변경 전 코드 없음"));
        CommonCode afterCode = commonCodeRepository.findById(afterCodeId).orElseThrow(() -> new IllegalArgumentException("변경 후 코드 없음"));

        // 3. 변경 신청 이력 저장
        ChangeHistory changeHistory = ChangeHistory.builder()
                .member(member)
                .beforeCode(beforeCode)
                .afterCode(afterCode)
                .grade(grade)
                .build();

        changeHistoryRepository.save(changeHistory);  // 변동 신청 이력 DB에 저장

        // 4. 필요한 경우 (예: 이메일, 알림 등) 추가 로직을 넣을 수 있음
    }

    public List<ChangeRequest> viewChangeRequest(){
        return changeRequestRepository.findAll();
    }
}
