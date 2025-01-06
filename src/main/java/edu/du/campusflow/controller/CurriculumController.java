package edu.du.campusflow.controller;

import edu.du.campusflow.entity.Curriculum;
import edu.du.campusflow.service.CurriculumService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CurriculumController {

   private final CurriculumService curriculumService;

   @GetMapping("/view/iframe/curriculum/register")
   public String searchCurriculum(
       @RequestParam(required = false) String year,
       @RequestParam(required = false) String grade,
       @RequestParam(required = false) String deptName,
       @RequestParam(required = false) String curriculumName,
       @RequestParam(required = false) String category,
       Model model) {

      // grade 입력값 변환
      if ("1학년".equals(grade)) grade = "GRADE_1";
      if ("2학년".equals(grade)) grade = "GRADE_2";
      if ("3학년".equals(grade)) grade = "GRADE_3";
      if ("4학년".equals(grade)) grade = "GRADE_4";

      List<Curriculum> results = curriculumService.searchCurriculum(year, grade, deptName, curriculumName, category);
      model.addAttribute("results", results);

      // View 파일 경로 업데이트
      return "view/iframe/curriculum/curriculum_register";
   }
}
