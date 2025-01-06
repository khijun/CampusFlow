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

    private final FileLoadService fileLoadService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("fileIds", fileLoadService.getAllImagesId());
        return "index";
    }

}