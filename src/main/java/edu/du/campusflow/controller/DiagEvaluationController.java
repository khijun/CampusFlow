package edu.du.campusflow.controller;

import edu.du.campusflow.dto.DiagQuestionDTO;
import edu.du.campusflow.entity.Dept;
import edu.du.campusflow.repository.CommonCodeRepository;
import edu.du.campusflow.repository.DeptRepository;
import edu.du.campusflow.repository.DiagItemRepository;
import edu.du.campusflow.service.DiagQuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/iframe/evaluation/diag")
@RequiredArgsConstructor
public class DiagEvaluationController {
    private final DiagQuestionService diagQuestionService;
    private final CommonCodeRepository commonCodeRepository;
    private final DiagItemRepository diagItemRepository;
    private final DeptRepository deptRepository;

    @GetMapping("/professor")
    public String showProfessorDiagnosticList(Model model) {
        log.info("교수용 진단평가 목록 조회");
        List<Map<String, Object>> lectures = diagQuestionService.getProfessorLectures();
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

        List<Map<String, Object>> lectures = diagQuestionService.getProfessorLectures();
        model.addAttribute("lectures", lectures);
        model.addAttribute("selectedOfregistrationId", ofregistrationId);

        List<DiagQuestionDTO> diagnosticResults = diagQuestionService.getDiagnosticResults(ofregistrationId);
        model.addAttribute("results", diagnosticResults);
        model.addAttribute("showResults", true);

        return "view/iframe/evaluation/diag/professor/diagQuestion";
    }

    @GetMapping("/admin")
    public String showAdminDiagnosticList(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Integer grade,
            @RequestParam(required = false) String lectureName,
            @RequestParam(required = false) String studentName,
            Model model
    ) {
        log.info("학과 정보 조회 시작");
        List<Dept> departments = deptRepository.findAll();
        model.addAttribute("departments", departments);

        if (department != null || grade != null ||
                !StringUtils.isEmpty(lectureName) || !StringUtils.isEmpty(studentName)) {
            // String을 Long으로 변환
            Long departmentId = department != null ? Long.parseLong(department) : null;

            List<DiagQuestionDTO> results = diagQuestionService
                    .getDiagnosticResultsBySearchCriteria(departmentId, grade, lectureName, studentName);
            model.addAttribute("results", results);
            model.addAttribute("showResults", true);
        }

        return "view/iframe/evaluation/diag/admin/diagQuestion";
    }
}
