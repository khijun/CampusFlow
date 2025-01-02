package edu.du.campusflow.service;

import edu.du.campusflow.entity.Member;
import edu.du.campusflow.entity.TuitionTarget;
import edu.du.campusflow.repository.TuitionRepository;
import edu.du.campusflow.repository.TuitionTargetRepository;
import edu.du.campusflow.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TuitionService {
    private final TuitionRepository tuitionRepository;
    private final TuitionTargetRepository tuitionTargetRepository;
    private final MemberService memberService;

    public TuitionTarget getTuitionTargetByMemberId(Long memberId) {
        Member member = memberService.findByMemberId(memberId);
        return tuitionTargetRepository.findByMember(member)
            .orElseThrow(() -> new RuntimeException("등록금 대상자 정보를 찾을 수 없습니다."));
    }
}