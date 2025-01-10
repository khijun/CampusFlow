package edu.du.campusflow.controller;

import edu.du.campusflow.dto.DiagQuestionDTO;
import edu.du.campusflow.service.DeptService;
import edu.du.campusflow.service.DiagEvaluationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
}