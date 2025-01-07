package edu.du.campusflow.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class MainController {

   @GetMapping("/")
   public String front_main(Model model) {
      model.addAttribute()
      return "view/main_view/front_main"; // 메인 페이지
   }

   @GetMapping("/student_main")
   public String studentPage() {
      return "view/main_view/student_main"; // 학생 페이지
   }

   @GetMapping("/professor_main")
   public String professorPage() {
      return "view/main_view/professor_main"; // 교수 페이지 }
   }

   @GetMapping("/staff_main")
   public String staffPage() {
      return "view/main_view/staff_main"; // 관리자 페이지
   }
}
