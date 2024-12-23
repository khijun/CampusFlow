package edu.du.academic_management_system.controller;

import edu.du.academic_management_system.service.AuthService;
import edu.du.academic_management_system.service.MemberService;
import edu.du.academic_management_system.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final AuthService authService;
    private final MemberService memberService;

    @GetMapping
    public String index(Model model) {
        Long memberId = authService.getCurrentMemberId();
        model.addAttribute("member", memberService.findByMemberId(memberId));
        return "index";
    }

}
