package edu.du.campusflow.service;

import edu.du.campusflow.entity.Member;
import edu.du.campusflow.repository.MemberRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final MemberRepository memberRepository;

    public AuthService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Long getCurrentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null||!authentication.isAuthenticated()) return null;
        return Long.parseLong(authentication.getName());
    }

    public Member getCurrentMember() {
        Long id = getCurrentMemberId();
        if(id == null) return null;
        return memberRepository.findById(id).orElse(null);
    }

}
