package edu.du.campusflow.controller;

import edu.du.campusflow.service.FileLoadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
<<<<<<< HEAD
//    private final AuthService authService;
//    private final MemberService memberService;
//
//    @GetMapping
//    public String index(Model model) {
//        Long memberId = authService.getCurrentMemberId();
//        model.addAttribute("member", memberService.findByMemberId(memberId));
//        return "index";
//    }
=======

    private final FileLoadService fileLoadService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("fileIds", fileLoadService.getAllImagesId());
        return "index";
    }
>>>>>>> 9758835aa10a6cc6f9e6f227832944b43715b6b9

}
