package edu.du.campusflow.controller;

import edu.du.campusflow.service.DeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DeptController {
    private final DeptService deptService;

    @GetMapping("/iframe/dept/view")
    public String deptView() {
        return "view/iframe/dept/dept_view";
    }
}
