package edu.du.campusflow.controller;

import edu.du.campusflow.dto.DiagEvaluationDetailDTO;
import edu.du.campusflow.dto.DiagQuestionDTO;
import edu.du.campusflow.service.DeptService;
import edu.du.campusflow.service.DiagEvaluationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/iframe/evaluation/diag")
@RequiredArgsConstructor
public class DiagEvaluationController {
    private final DiagEvaluationService diagEvaluationService;
    private final DeptService deptService;

    @GetMapping("/professor")
    public String showProfessorDiagnosticList(Model model) {
        log.info("교수용 진단평가 목록 조회");
        List<Map<String, Object>> lectures = diagEvaluationService.getProfessorLectures();
        model.addAttribute("lectures", lectures);
        model.addAttribute("showResults", false);
        return "view/iframe/evaluation/diag/professor/diagQuestion";
    }

    @GetMapping("/professor/{ofregistrationId}")
    public String showProfessorDiagnosticResults(
            @PathVariable Long ofregistrationId,
            Model model
    ) {
        log.info("교수용 진단평가 결과 조회 - 강의 ID: {}", ofregistrationId);

        List<Map<String, Object>> lectures = diagEvaluationService.getProfessorLectures();
        model.addAttribute("lectures", lectures);
        model.addAttribute("selectedOfregistrationId", ofregistrationId);

        List<DiagQuestionDTO> diagnosticResults = diagEvaluationService.getDiagnosticResults(ofregistrationId);
        model.addAttribute("results", diagnosticResults);
        model.addAttribute("showResults", true);

        return "view/iframe/evaluation/diag/professor/diagQuestion";
    }

    @GetMapping("/admin")
    public String showAdminDiagnosticList(Model model) {
        log.info("관리자용 진단평가 목록 조회");
        try {
            List<Map<String, Object>> lectures = diagEvaluationService.getAllLectures();
            log.info("조회된 강의 수: {}", lectures.size());
            model.addAttribute("lectures", lectures);
            model.addAttribute("showResults", false);
            return "view/iframe/evaluation/diag/admin/diagQuestion";
        } catch (Exception e) {
            log.error("진단평가 목록 조회 중 오류 발생", e);
            throw e;
        }
    }

    @GetMapping("/admin/{ofregistrationId}")
    public String showAdminDiagnosticResults(
            @PathVariable Long ofregistrationId,
            Model model
    ) {
        log.info("관리자용 진단평가 결과 조회 - 강의 ID: {}", ofregistrationId);

        List<Map<String, Object>> lectures = diagEvaluationService.getAllLectures();
        model.addAttribute("lectures", lectures);
        model.addAttribute("selectedOfregistrationId", ofregistrationId);

        List<DiagQuestionDTO> diagnosticResults = diagEvaluationService.getDiagnosticResults(ofregistrationId);
        model.addAttribute("results", diagnosticResults);
        model.addAttribute("showResults", true);

        return "view/iframe/evaluation/diag/admin/diagQuestion";
    }

    // 학과 목록 조회
    @GetMapping("/departments")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getDepartments() {
        return ResponseEntity.ok(diagEvaluationService.getAllDepartments());
    }

    // 1. 학과/학년 검색
    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<List<DiagEvaluationDetailDTO>> searchEvaluations(
            @RequestParam Long deptId,
            @RequestParam String grade,
            @RequestParam(required = false) String lectureName,
            @RequestParam(required = false) String studentName) {

        log.info("Search params - deptId: {}, grade: {}, lectureName: {}, studentName: {}",
                deptId, grade, lectureName, studentName);

        List<DiagEvaluationDetailDTO> results = diagEvaluationService.searchEvaluations(
                deptId, grade, lectureName, studentName);

        log.info("Search results size: {}", results.size());
        return ResponseEntity.ok(results);
    }
}