package edu.du.campusflow.service;

import edu.du.campusflow.entity.Member;
import edu.du.campusflow.repository.MemberRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

   private final MemberRepository memberRepository;

   public AuthService(MemberRepository memberRepository) {
      this.memberRepository = memberRepository;
   }

   public boolean isAnonymousUser(Authentication authentication) {
      if (authentication == null || !authentication.isAuthenticated()) {
         // 인증되지 않은 경우
         return true;
      }

      // 익명 사용자 토큰인지 확인
      return authentication instanceof AnonymousAuthenticationToken;
   }

   public Long getCurrentMemberId() {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication == null || authentication.getName().isEmpty() || !authentication.isAuthenticated() || isAnonymousUser(authentication))
         return null;
      System.out.println(authentication.getName());
      return Long.parseLong(authentication.getName());
   }

   public Member getCurrentMember() {
      Long id = getCurrentMemberId();
      if (id == null) return null;
      return memberRepository.findById(id).orElse(null);
   }

}
