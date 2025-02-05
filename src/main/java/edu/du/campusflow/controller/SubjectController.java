package edu.du.campusflow.controller;

import edu.du.campusflow.dto.SubjectDTO;
import edu.du.campusflow.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class SubjectController {
   private final SubjectService subjectService;

   @GetMapping("/iframe/curriculum/subject/list")
   public String selectSubject(Model model) {
      model.addAttribute("filter", null);
      return "view/iframe/curriculum/subject/subject_list";
   }

   @GetMapping("/iframe/curriculum/subject/register")
   @PreAuthorize("hasAnyRole('STAFF', 'PROFESSOR')")
   public String registerSubject(Model model) {
      return "view/iframe/curriculum/subject/subject_register";
   }

   @GetMapping("/iframe/curriculum/subject/update")
   @PreAuthorize("hasAnyRole('STAFF', 'PROFESSOR')")
   public String updateSubject(Model model) {
      return "view/iframe/curriculum/subject/subject_update";
   }

   @GetMapping("/api/subjects")
   public ResponseEntity<?> getSubjects(@RequestParam(required = false) String keyword) {
      return ResponseEntity.ok(SubjectDTO.fromEntityList(subjectService.findSubjects(keyword)));
   }

   @GetMapping("/api/subjects/search")
   public ResponseEntity<List<SubjectDTO>> searchSubjects(@RequestParam String keyword) {
      List<SubjectDTO> subjects = SubjectDTO.fromEntityList(subjectService.searchSubjects(keyword));
      return ResponseEntity.ok(subjects);
   }

   @PostMapping("/api/subjects")
   @PreAuthorize("hasAnyRole('STAFF', 'PROFESSOR')")
   public ResponseEntity<?> registerSubjects(@RequestBody List<SubjectDTO> subjects) {
      subjectService.saveAll(subjects);
      return ResponseEntity.ok(Map.of("status", "success", "message", "Subjects saved successfully."));
   }

   @PutMapping("/api/subjects/update")
   @PreAuthorize("hasAnyRole('STAFF', 'PROFESSOR')")
   public ResponseEntity<?> updateSubjects(@RequestBody List<SubjectDTO> updatedSubjects) {
      subjectService.updateSubjects(updatedSubjects);
      return ResponseEntity.ok(Map.of("status", "success", "message", "Subjects updated successfully."));
   }

   @DeleteMapping("/api/subjects/delete")
   @PreAuthorize("hasAnyRole('STAFF', 'PROFESSOR')")
   public ResponseEntity<?> deleteSubjects(@RequestBody List<Long> ids) {
      subjectService.deleteSubjects(ids);
      return ResponseEntity.ok(Map.of("status", "success", "message", "Subjects deleted successfully."));
   }

   @GetMapping("/api/subjects/all")
   public ResponseEntity<List<SubjectDTO>> getAllSubjects() {
      List<SubjectDTO> subjects = subjectService.getAllSubjects();
      return ResponseEntity.ok(subjects);
   }

   @GetMapping("/api/subjects/by-dept/{deptId}")
   public ResponseEntity<List<SubjectDTO>> getSubjectsByDept(@PathVariable Long deptId) {
      if (deptId == null) {
         return ResponseEntity.badRequest().body(Collections.emptyList());
      }
      List<SubjectDTO> subjects = subjectService.getSubjectsByDept(deptId);
      if (subjects.isEmpty()) {
         System.out.println("해당 학과에 등록된 과목이 없습니다.");
      } else {
         System.out.println("조회된 과목 리스트: " + subjects);
      }
      return ResponseEntity.ok(subjects);
   }
}
