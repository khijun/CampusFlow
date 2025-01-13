package edu.du.campusflow.controller;

import edu.du.campusflow.dto.CurriculumDTO;
import edu.du.campusflow.entity.Curriculum;
import edu.du.campusflow.entity.Subject;
import edu.du.campusflow.repository.CommonCodeGroupRepository;
import edu.du.campusflow.service.CurriculumService;
import edu.du.campusflow.service.DeptService;
import edu.du.campusflow.service.SubjectService;
import edu.du.campusflow.repository.CommonCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/iframe/curriculum")
public class CurriculumController {

   private final CurriculumService curriculumService;
   private final DeptService deptService;
   private final SubjectService subjectService;
   private final CommonCodeRepository commonCodeRepository; // 공통코드 Repository 추가
   private final CommonCodeGroupRepository commonCodeGroupRepository;

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

   // POST 요청 처리: 등록
   @PostMapping("/register")
   public String createCurriculum(@ModelAttribute CurriculumDTO dto) {
      curriculumService.createCurriculum(dto);
      return "redirect:/iframe/curriculum/register";
   }

   @PostMapping("/register-subject")
   public String addCurriculumSubject(
       @RequestParam Long curriculumId,
       @RequestParam Long subjectId,
       @RequestParam(required = false) Long prereqSubjectId,
       @RequestParam(required = false) String semester,
       @RequestParam(required = false) String subjectType
   ) {
      curriculumService.addCurriculumSubject(curriculumId, subjectId, prereqSubjectId, semester, subjectType);
      return "redirect:/iframe/curriculum/register";
   }


   @GetMapping("/subjects")
   @ResponseBody
   public List<Subject> searchSubjects(@RequestParam String keyword) {
      return subjectService.searchSubjects(keyword);
   }
}
