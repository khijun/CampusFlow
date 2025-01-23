package edu.du.campusflow.controller;

import edu.du.campusflow.dto.SubjectDTO;
import edu.du.campusflow.entity.Subject;
import edu.du.campusflow.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
   public String registerSubject(Model model) {
      return "view/iframe/curriculum/subject/subject_register";
   }

   @GetMapping("/iframe/curriculum/subject/update")
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
   public ResponseEntity<?> registerSubjects(@RequestBody List<SubjectDTO> subjects) {
      subjectService.saveAll(subjects);
      return ResponseEntity.ok(Map.of("status", "success", "message", "Subjects saved successfully."));
   }

   @PutMapping("/api/subjects/update")
   public ResponseEntity<?> updateSubjects(@RequestBody List<SubjectDTO> updatedSubjects) {
      subjectService.updateSubjects(updatedSubjects);
      return ResponseEntity.ok(Map.of("status", "success", "message", "Subjects updated successfully."));
   }

   @DeleteMapping("/api/subjects/delete")
   public ResponseEntity<?> deleteSubjects(@RequestBody List<Long> ids) {
      subjectService.deleteSubjects(ids);
      return ResponseEntity.ok(Map.of("status", "success", "message", "Subjects deleted successfully."));
   }

   @GetMapping("/api/subjects/all")
   public ResponseEntity<List<SubjectDTO>> getAllSubjects() {
      List<SubjectDTO> subjects = subjectService.getAllSubjects();
      return ResponseEntity.ok(subjects);
   }
}
