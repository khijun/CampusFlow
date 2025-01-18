package edu.du.campusflow.controller;

import edu.du.campusflow.dto.MemberDTO;
import edu.du.campusflow.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class MainController {

   private final AuthService authService;

   public MainController(AuthService authService) {
      this.authService = authService;
   }

   @GetMapping("/")
   public String frontMain(Model model) {
      model.addAttribute("memberDTO", MemberDTO.fromEntity(authService.getCurrentMember()));
      return "view/main_view/front_main"; // 메인 페이지
   }
}
