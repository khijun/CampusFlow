package edu.du.campusflow.controller;

import edu.du.campusflow.dto.CurriculumDTO;
import edu.du.campusflow.service.CurriculumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CurriculumController {
   private final CurriculumService curriculumService;

   // 교육과정 목록 페이지 연결
   @GetMapping("/iframe/curriculum/list")
   public String getCurriculumListPage() {
      return "view/iframe/curriculum/curriculum_list";
   }

   // 교육과정 등록 페이지 연결
   @GetMapping("/iframe/curriculum/register")
   @PreAuthorize("hasAnyRole('STAFF')")
   public String getCurriculumRegisterPage() {
      return "view/iframe/curriculum/curriculum_register";
   }

   @GetMapping("/iframe/curriculum/update")
   @PreAuthorize("hasAnyRole('STAFF')")
   public String getCurriculumUpdatePage() { return "view/iframe/curriculum/curriculum_update"; }

   // 교육과정 리스트 조회 (검색 포함)
   @GetMapping("/api/curriculums")
   public ResponseEntity<List<CurriculumDTO>> getCurriculums(@RequestParam(required = false) String keyword) {
      List<CurriculumDTO> curriculumList = curriculumService.getCurriculums(keyword);
      return ResponseEntity.ok(curriculumList);
   }

   // 교육과정 등록 API
   @PostMapping("/api/curriculums")
   @PreAuthorize("hasAnyRole('STAFF')")
   public ResponseEntity<?> registerCurriculums(@RequestBody List<CurriculumDTO> curriculums) {
      curriculumService.saveCurriculums(curriculums);
      return ResponseEntity.ok().body("{\"message\": \"교육과정이 성공적으로 등록되었습니다.\"}");
   }

   @PutMapping("/api/curriculums/update")
   @PreAuthorize("hasAnyRole('STAFF')")
   public ResponseEntity<?> updateCurriculums(@RequestBody List<CurriculumDTO> updatedCurriculums) {
      curriculumService.updateCurriculums(updatedCurriculums);
      return ResponseEntity.ok().body("{\"message\": \"교육과정이 성공적으로 수정되었습니다.\"}");
   }

   @DeleteMapping("/api/curriculums/delete")
   @PreAuthorize("hasAnyRole('STAFF')")
   public ResponseEntity<?> deleteCurriculums(@RequestBody List<Long> curriculumIds) {
      curriculumService.deleteCurriculums(curriculumIds);
      return ResponseEntity.ok().body("{\"message\": \"교육과정이 성공적으로 삭제되었습니다.\"}");
   }

}
