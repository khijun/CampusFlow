package edu.du.campusflow.controller;

import edu.du.campusflow.entity.ChangeRequest;
import edu.du.campusflow.entity.Member;
import edu.du.campusflow.service.AuthService;
import edu.du.campusflow.service.ChangeRequestService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ChangeRequestController {

    private final ChangeRequestService changeRequestService;
    private final AuthService authService;

    public ChangeRequestController(ChangeRequestService changeRequestService, AuthService authService) {
        this.changeRequestService = changeRequestService;
        this.authService = authService;
    }

    // 학적 조회
    @GetMapping("/iframe/academic/change-request-list")
    public String getChangeRequests(Model model) {
        List<ChangeRequest> changeRequests = changeRequestService.getChangeRequestsByMemberId(authService.getCurrentMemberId());
        model.addAttribute("changeRequests", changeRequests);
        return "view/iframe/academic/change-request-list"; // 학적 조회 페이지
    }

    // 휴학/복학 신청 폼
    @GetMapping("/iframe/academic/change-request-form")
    public String changeRequestForm() {
        return "view/iframe/academic/change-request-form"; // 휴학/복학 신청 페이지
    }

    @PostMapping("/iframe/academic/process-change-request")
    public String proequest(ChangeRequest changeRequest, Long gradeCodeId, Long newStatusCodeId) {
        changeRequestService.processChangeRequest(changeRequest, gradeCodeId, newStatusCodeId);
        return "redirect:/iframe/academic/change-request-list"; // 신청 후 목록으로 리다이렉트
    }
}
