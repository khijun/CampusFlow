package edu.du.campusflow.controller;

import edu.du.campusflow.dto.SubjectDTO;
import edu.du.campusflow.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class SubjectController {
   private final SubjectService subjectService;

   @GetMapping("/iframe/curriculum/subject/list")
   public String selectSubject(Model model) {
      // 초기 화면 렌더링 시 필요한 데이터를 설정 (현재는 빈 필터)
      model.addAttribute("filter", null);
      return "view/iframe/curriculum/subject/subject_list"; // JSP 또는 HTML 파일 경로
   }

   @GetMapping("/api/subjects")
   public ResponseEntity<?> getSubjects(@RequestParam(required = false) String keyword) {
      // 검색어가 있을 경우 검색어 기반 조회, 없을 경우 전체 조회
      return ResponseEntity.ok(SubjectDTO.fromEntityList(subjectService.findSubjects(keyword)));
   }
}
