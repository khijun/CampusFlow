package edu.du.campusflow.service;

import edu.du.campusflow.entity.Member;
import edu.du.campusflow.repository.MemberRepository;
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
