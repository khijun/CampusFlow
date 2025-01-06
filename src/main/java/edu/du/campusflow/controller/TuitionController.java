package edu.du.campusflow.controller;

import edu.du.campusflow.dto.TuitionDTO;
import edu.du.campusflow.service.AuthService;
import edu.du.campusflow.service.TuitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tuition")
@RequiredArgsConstructor
public class TuitionController {

    private final TuitionService tuitionService;
    private final AuthService authService;  // AuthService 추가

    @GetMapping("/tuition/tuitionTarget")
    public String showTuitionTarget(Model model) {
        Long memberId = authService.getCurrentMemberId();
        TuitionDTO tuitionTarget = tuitionService.getTuitionDTOByMemberId(memberId);
        model.addAttribute("tuitionTarget", tuitionTarget);
        return "tuition/tuitionTarget";
    }
} 