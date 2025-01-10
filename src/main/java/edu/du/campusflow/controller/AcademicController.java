package edu.du.campusflow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AcademicController {
    @GetMapping("/iframe/academic/view")
    public String academicView(){
        return "/view/iframe/academic/academic_view";
    }
    @GetMapping("/iframe/academic/leavereturn")
    public String leaveReturnApplication(){
        return "/view/iframe/academic/academic_leavereturn";
    }
    @GetMapping("/iframe/academic/transdraw")
    public String transDrawApplication(){
        return "/view/iframe/academic/academic_transdraw";
    }
}
