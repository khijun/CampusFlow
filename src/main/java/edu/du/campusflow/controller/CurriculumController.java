package edu.du.campusflow.controller;

import edu.du.campusflow.entity.Curriculum;
import edu.du.campusflow.service.CurriculumService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/iframe/curriculum")
public class CurriculumController {

   private final CurriculumService curriculumService;

   // GET 요청 처리: 검색
   @GetMapping("/register")
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

      return "view/iframe/curriculum/curriculum_register";
   }

   // POST 요청 처리: 데이터 삽입
//   @PostMapping("/register")
//   @ResponseBody
//   public String insertCurriculum(@RequestBody Curriculum curriculum) {
//      curriculumService.saveCurriculum(curriculum);
//      return "등록 성공!";
//   }

   // PUT 요청 처리: 데이터 수정
//   @PutMapping("/register")
//   @ResponseBody
//   public String updateCurriculum(@RequestBody Curriculum curriculum) {
//      curriculumService.updateCurriculum(curriculum);
//      return "수정 성공!";
//   }
}
