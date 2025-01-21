package edu.du.campusflow.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/session")
public class SessionController {

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> extendSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            // 세션 연장
            return ResponseEntity.ok("세션 연장 성공");
        } else {
            return ResponseEntity.badRequest().body("세션 연장 실패");
        }
    }
}