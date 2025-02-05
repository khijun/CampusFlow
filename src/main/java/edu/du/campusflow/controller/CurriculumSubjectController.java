package edu.du.campusflow.controller;

import edu.du.campusflow.dto.CurriculumSubjectDTO;
import edu.du.campusflow.dto.CurriculumSubjectDetailDTO;
import edu.du.campusflow.dto.CurriculumSubjectRegisterDTO;
import edu.du.campusflow.service.CurriculumSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Controller
public class CurriculumSubjectController {

    @Autowired
    CurriculumSubjectService curriculumSubjectService;

    @GetMapping("/iframe/curriculum/curriculum-subject/list")
    public String getCurriculumSubjectListPage() {
        return "view/iframe/curriculum/curriculum-subject/curriculumSubject_list";  // curriculumSubject_list.html 반환
    }
    @GetMapping("/iframe/curriculum/curriculum-subject/update")
    @PreAuthorize("hasAnyRole('STAFF')")
    public String getCurriculumSubjectUpdatePage() {
        return "view/iframe/curriculum/curriculum-subject/curriculumSubject_update";  // curriculumSubject_update.html 반환
    }

    @GetMapping("/iframe/curriculum/curriculum-subject/register")
    @PreAuthorize("hasAnyRole('STAFF')")
    public String getCurriculumSubjectRegisterPage() {
        return "view/iframe/curriculum/curriculum-subject/curriculumSubject_register";
    }

    //강의 개설 페이지에서 사용할 검색
    @GetMapping("/api/curriculum-subjects/search")
    public ResponseEntity<List<CurriculumSubjectDTO>> searchCurriculumSubjects(
            @RequestParam(required = false) String deptName,
            @RequestParam(required = false) String curriculumName,
            @RequestParam(required = false) String semesterCode,
            @RequestParam(required = false) Integer curriculumYear) {
        List<CurriculumSubjectDTO> subjects = curriculumSubjectService.searchCurriculumSubjectBySubjectName(deptName, curriculumName, semesterCode, curriculumYear);
        return ResponseEntity.ok(subjects);
    }

    // 교육과정 교과목 조회
    @GetMapping("/api/curriculum-subjects")
    public ResponseEntity<List<CurriculumSubjectDetailDTO>> getCurriculumSubjects(
        @RequestParam(required = false) String keyword) {
        List<CurriculumSubjectDetailDTO> subjects = curriculumSubjectService.getCurriculumSubjects(keyword);
        return ResponseEntity.ok(subjects);
    }

    // 교육과정 교과목 수정
    @PutMapping("/api/curriculum-subjects/update")
    @PreAuthorize("hasAnyRole('STAFF')")
    public ResponseEntity<?> updateCurriculumSubjects(@RequestBody List<CurriculumSubjectDetailDTO> updatedSubjects) {
        curriculumSubjectService.updateCurriculumSubjects(updatedSubjects);
        return ResponseEntity.ok().body("{\"message\": \"교육과정 교과목이 성공적으로 수정되었습니다.\"}");
    }

    // 교육과정 교과목 삭제
    @DeleteMapping("/api/curriculum-subjects/delete")
    @PreAuthorize("hasAnyRole('STAFF')")
    public ResponseEntity<?> deleteCurriculumSubjects(@RequestBody List<Long> curriculumSubjectIds) {
        curriculumSubjectService.deleteCurriculumSubjects(curriculumSubjectIds);
        return ResponseEntity.ok().body("{\"message\": \"교육과정 교과목이 성공적으로 삭제되었습니다.\"}");
    }

    @PostMapping("/api/curriculum-subjects/register")
    @PreAuthorize("hasAnyRole('STAFF')")
    public ResponseEntity<?> registerCurriculumSubjects(@RequestBody List<CurriculumSubjectRegisterDTO> curriculumSubjects) {
        curriculumSubjectService.registerCurriculumSubjects(curriculumSubjects);
        return ResponseEntity.ok().body("{\"message\": \"교육과정 교과목이 성공적으로 추가되었습니다.\"}");
    }
}
