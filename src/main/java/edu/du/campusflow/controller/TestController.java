package edu.du.campusflow.controller;

import edu.du.campusflow.service.AuthService;
import edu.du.campusflow.service.FileUploadService;
import edu.du.campusflow.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final AuthService authService;
    private final MemberService memberService;
    private final FileUploadService fileUploadService;

    @GetMapping
    public String index(Model model) {
        Long memberId = authService.getCurrentMemberId();
        model.addAttribute("member", memberService.findByMemberId(memberId));
        return "index";
    }

    @PostMapping("/files")
    public String fileUpload(@RequestParam("file") MultipartFile file, Model model) {
        fileUploadService.saveFile(file);
        return "redirect:/test";
    }

}
