package edu.du.academic_management_system.service;

import edu.du.academic_management_system.entity.Member;
import edu.du.academic_management_system.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member findByMemberId(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow();
    };
}
