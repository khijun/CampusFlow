package edu.du.campusflow.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public Long getCurrentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null||!authentication.isAuthenticated()) return null;
        return Long.parseLong(authentication.getName());
    }


}
