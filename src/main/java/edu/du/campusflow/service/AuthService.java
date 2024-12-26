package edu.du.campusflow.service;

import edu.du.campusflow.exception.NotLoggedInException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public Long getCurrentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null||!authentication.isAuthenticated()) throw new NotLoggedInException("로그인되지 않아 현재 멤버를 가져올 수 없음.");
        return Long.parseLong(authentication.getName());
    }


}
