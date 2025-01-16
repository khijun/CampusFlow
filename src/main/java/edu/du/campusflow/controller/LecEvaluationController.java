package edu.du.campusflow.controller;

import edu.du.campusflow.dto.LecQuestionDTO;
import edu.du.campusflow.service.DiagEvaluationService;
import edu.du.campusflow.service.LecQuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/iframe/evaluation/lec")
@RequiredArgsConstructor
public class LecEvaluationController {

    private final LecQuestionService lecQuestionService;
    private final DiagEvaluationService diagEvaluationService;

    // 교직원용 강의평가 페이지 이동
    @GetMapping("/admin")
    public String showAdminLecList() {
        return "view/iframe/evaluation/lec/admin/adminLec";
    }

    // 관리자용 학과 목록 조회
    @GetMapping("/admin/departments")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getDepartments() {
        return ResponseEntity.ok(lecQuestionService.getAllDepartments());
    }

    // 관리자용 학과/학년 검색
    @GetMapping("/admin/search")
    @ResponseBody
    public ResponseEntity<List<LecQuestionDTO>> searchEvaluations(
            @RequestParam Long deptId,
            @RequestParam String grade,
            @RequestParam(required = false) String lectureName,
            @RequestParam(required = false) String name) {

        log.info("Search params - deptId: {}, grade: {}, lectureName: {}, name: {}",
                deptId, grade, lectureName, name);

        List<LecQuestionDTO> results = lecQuestionService.searchEvaluations(
                deptId, grade, lectureName, name);

        log.info("Search results size: {}", results.size());
        return ResponseEntity.ok(results);
    }

    // 교수용 강의평가 페이지 이동
    @GetMapping("/professor")
    public String showProfessorDiagnosticList() {
        return "view/iframe/evaluation/lec/professor/professorLec";
    }

    // 교수의 학과 정보 조회
    @GetMapping("/professor/departments")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getProfessorDepartment() {
        log.info("Getting professor department info");  // 로그 추가
        return ResponseEntity.ok(diagEvaluationService.getProfessorDepartment());
    }

    // 교수용 학과/학년 검색
    @GetMapping("/professor/search")
    @ResponseBody
    public ResponseEntity<List<LecQuestionDTO>> searchProfessorEvaluations(
            @RequestParam Long deptId,
            @RequestParam String grade,
            @RequestParam(required = false) String lectureName,
            @RequestParam(required = false) String name) {

        log.info("Professor Search params - deptId: {}, grade: {}, lectureName: {}, name: {}",
                deptId, grade, lectureName, name);

        List<LecQuestionDTO> results = lecQuestionService.searchEvaluations(
                deptId, grade, lectureName, name);

        log.info("Professor Search results size: {}", results.size());
        return ResponseEntity.ok(results);
    }

}