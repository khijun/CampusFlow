package edu.du.campusflow.controller;

import edu.du.campusflow.entity.Curriculum;
import edu.du.campusflow.entity.Subject;
import edu.du.campusflow.service.CurriculumService;
import edu.du.campusflow.service.DeptService;
import edu.du.campusflow.service.SubjectService;
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
   private final DeptService deptService;
   private final SubjectService subjectService;

   @GetMapping("/register")
   public String searchCurriculum(
       @RequestParam(required = false) String year,
       @RequestParam(required = false) String grade,
       @RequestParam(required = false) String deptName,
       @RequestParam(required = false) String curriculumName,
       @RequestParam(required = false) String category,
       Model model) {

      // 학과 리스트 추가
      model.addAttribute("departments", deptService.getAllDepartments());

      // 검색 결과 추가
      model.addAttribute("results", curriculumService.searchCurriculum(year, grade, deptName, curriculumName, category));

      return "view/iframe/curriculum/curriculum_register";
   }

   @PostMapping("/register")
   public String createCurriculum(
       @ModelAttribute Curriculum curriculum,
       @RequestParam(required = false) Long subjectId,
       @RequestParam(required = false) Long prereqSubjectId) {

      curriculumService.createCurriculum(curriculum, subjectId, prereqSubjectId);
      return "redirect:/iframe/curriculum/register";
   }

   @GetMapping("/subjects")
   @ResponseBody
   public List<Subject> searchSubjects(@RequestParam String keyword) {
      return subjectService.searchSubjects(keyword);
   }

}
