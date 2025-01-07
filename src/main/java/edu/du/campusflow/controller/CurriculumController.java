package edu.du.campusflow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CurriculumController {

   // 교육과정 등록 페이지
   @GetMapping("/iframe/curriculum/register")
   public String curriculumRegister() {
      return "view/iframe/curriculum/curriculum_register"; // HTML 파일 경로
   }

   // 교육과정 수정 페이지
   @GetMapping("/iframe/curriculum/update")
   public String curriculumUpdate() {
      return "view/iframe/curriculum/curriculum_update"; // HTML 파일 경로
   }

   // 교육과정 승인 페이지
   @GetMapping("/iframe/curriculum/approval")
   public String curriculumApproval() {
      return "view/iframe/curriculum/curriculum_approval"; // HTML 파일 경로
   }

   // 교육과정 개편 이력 페이지
   @GetMapping("/iframe/curriculum/history")
   public String curriculumHistory() {
      return "view/iframe/curriculum/curriculum_history"; // HTML 파일 경로
   }
}
