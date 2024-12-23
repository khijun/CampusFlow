package edu.du.academic_management_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private MemberService memberService;

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            // UserDetails 객체에서 사용자 이름 가져오기
            return authentication.getName(); // 로그인한 사용자의 username 반환
        }
        return null;
    }


}
