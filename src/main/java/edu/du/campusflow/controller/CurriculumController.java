package edu.du.campusflow.controller;

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

   @PostMapping("/register")
   public String createCurriculum(
       @RequestParam Long deptId,
       @RequestParam String curriculumName,
       @RequestParam Integer year,
       @RequestParam Integer gradeCapacity,
       @RequestParam String curriculumStatus, // code_value로 받음
       @RequestParam String grade,           // code_value로 받음
       @RequestParam(required = false) String semester, // code_value로 받음
       @RequestParam(required = false) String subjectType, // code_value로 받음
       @RequestParam(required = false) String dayNight, // code_value로 받음
       @RequestParam(required = false) String gradingMethod, // code_value로 받음
       @RequestParam(required = false) String reason, // 사유 추가
       @RequestParam(required = false) Long subjectId,
       @RequestParam(required = false) Long prereqSubjectId,
       Model model) {

      Curriculum curriculum = new Curriculum();
      curriculum.setDept(deptService.getDepartmentById(deptId));
      curriculum.setCurriculumName(curriculumName);
      curriculum.setCurriculumYear(year);
      curriculum.setGradeCapacity(gradeCapacity);
      curriculum.setCreatedAt(LocalDateTime.now());
      curriculum.setUpdatedAt(LocalDateTime.now());
      curriculum.setReason(reason); // 사유 매핑

      // 공통코드 매핑
      curriculum.setCurriculumStatus(curriculumService.findCommonCode("CURRICULUMSTATUS", curriculumStatus));
      curriculum.setGrade(curriculumService.findCommonCode("GRADE", grade));
      curriculum.setDayNight(curriculumService.findCommonCode("DAY_NIGHT", dayNight));
      curriculum.setGradingMethod(curriculumService.findCommonCode("GRADING_METHOD", gradingMethod));

      curriculumService.createCurriculum(curriculum, subjectId, prereqSubjectId, semester, subjectType);
      return "redirect:/iframe/curriculum/register";
   }

   @GetMapping("/subjects")
   @ResponseBody
   public List<Subject> searchSubjects(@RequestParam String keyword) {
      return subjectService.searchSubjects(keyword);
   }
}
