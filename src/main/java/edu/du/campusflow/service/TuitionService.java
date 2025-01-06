package edu.du.campusflow.service;

import edu.du.campusflow.entity.Member;
import edu.du.campusflow.entity.TuitionTarget;
import edu.du.campusflow.repository.TuitionRepository;
import edu.du.campusflow.repository.TuitionTargetRepository;
import edu.du.campusflow.service.MemberService;
import edu.du.campusflow.dto.TuitionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TuitionService {
    private final TuitionRepository tuitionRepository;
    private final TuitionTargetRepository tuitionTargetRepository;
    private final MemberService memberService;

    /**
     * 회원 ID로 등록금 정보를 조회하여 DTO로 변환
     * @param memberId 조회할 회원의 ID
     * @return 등록금 정보가 담긴 DTO 객체
     * @throws RuntimeException 등록금 대상자 정보가 없는 경우
     */
    public TuitionDTO getTuitionDTOByMemberId(Long memberId) {
        Member member = memberService.findByMemberId(memberId);
        TuitionTarget tuitionTarget = tuitionTargetRepository.findByMember(member)
            .orElseThrow(() -> new RuntimeException("등록금 대상자 정보를 찾을 수 없습니다."));

        return TuitionDTO.builder()
            .targetId(tuitionTarget.getTargetId())
            .memberId(member.getMemberId())
            .memberName(member.getName())
            .department(member.getDept().getDeptName())
            .amount(tuitionTarget.getTuitionId().getAmount())
            .paidAmount(tuitionTarget.getPaidAmount().intValue())
            .paidDate(tuitionTarget.getPaidDate())
            .paymentStatus(tuitionTarget.isPaymentStatus())
            .build();
    }
}