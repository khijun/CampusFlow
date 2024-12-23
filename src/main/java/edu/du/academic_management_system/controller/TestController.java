package edu.du.academic_management_system.controller;

import edu.du.academic_management_system.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final StudentService studentService;
    @GetMapping
    public String index() {
        return "index";
    }

}
