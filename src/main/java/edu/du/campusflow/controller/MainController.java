package edu.du.campusflow.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class MainController {

   @GetMapping("/")
   public String front_main() {
      return "view/main_view/front_main"; // 메인 페이지
   }

   @GetMapping("/student_main")
   public String studentPage() {
      return "view/main_view/student_main"; // 학생 페이지
   }

   @GetMapping("/professor_main")
   public String professorPage(HttpSession session, Model model) {

      model.addAttribute("memberId");
      return "view/main_view/professor_main";
   }


   @GetMapping("/staff_main")
   public String staffPage() {
      return "view/main_view/staff_main"; // 관리자 페이지
   }
}
