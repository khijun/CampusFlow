package edu.du.campusflow.controller;

import edu.du.campusflow.dto.CurriculumDTO;
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

   @GetMapping("/update")
   public String updateCurriculum() {
      return "view/iframe/curriculum/curriculum_update";
   }

   // POST 요청 처리: 등록
   @PostMapping("/register")
   public String createCurriculum(@ModelAttribute CurriculumDTO dto) {
      log.info("Received Subject IDs: {}", dto.getSubjectIds());
      log.info("Received Semesters: {}", dto.getSemesters());

      if (dto.getSubjectIds() == null || dto.getSubjectIds().isEmpty()) {
         throw new IllegalArgumentException("Subject IDs are missing.");
      }

      curriculumService.createCurriculum(dto);
      return "redirect:/iframe/curriculum/register";
   }


   @PostMapping("/register-subject")
   public String addCurriculumSubject(
       @RequestParam Long curriculumId,
       @RequestParam Long subjectId,
       @RequestParam String semester // 필수 매개변수로 변경
   ) {
      // 유효성 검증
      if (semester == null || semester.isEmpty()) {
         throw new IllegalArgumentException("Semester is required");
      }

      // 서비스 호출
      curriculumService.addCurriculumSubject(curriculumId, subjectId, semester);

      // 리다이렉트
      return "redirect:/iframe/curriculum/register";
   }

   @GetMapping("/subjects")
   @ResponseBody
   public List<Subject> searchSubjects(@RequestParam String keyword) {
      return subjectService.searchSubjects(keyword);
   }
}
