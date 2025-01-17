package edu.du.campusflow.controller;

import edu.du.campusflow.dto.CurriculumDTO;
import edu.du.campusflow.service.CurriculumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
   public String getCurriculumListPage(Model model) {
      return "view/iframe/curriculum/curriculum_list";
   }

   // 교육과정 등록 페이지 연결
   @GetMapping("/iframe/curriculum/register")
   public String getCurriculumRegisterPage(Model model) {
      return "view/iframe/curriculum/curriculum_register";
   }

   // 교육과정 리스트 조회 (검색 포함)
   @GetMapping("/api/curriculums")
   public ResponseEntity<List<CurriculumDTO>> getCurriculums(@RequestParam(required = false) String keyword) {
      List<CurriculumDTO> curriculumList = curriculumService.getCurriculums(keyword);
      return ResponseEntity.ok(curriculumList);
   }

   // 교육과정 등록 API
   @PostMapping("/api/curriculums")
   public ResponseEntity<String> registerCurriculums(@RequestBody List<CurriculumDTO> curriculums) {
      curriculumService.saveCurriculums(curriculums);
      return ResponseEntity.ok("교육과정이 성공적으로 등록되었습니다.");
   }
}
