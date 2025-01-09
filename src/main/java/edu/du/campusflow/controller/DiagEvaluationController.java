package edu.du.campusflow.controller;

import edu.du.campusflow.dto.DiagQuestionDTO;
import edu.du.campusflow.entity.Dept;
import edu.du.campusflow.repository.DeptRepository;
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
            @RequestParam(required = false) String grade,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String lectureName,
            @RequestParam(required = false) String studentName,
            Model model
    ) {
        log.info("관리자용 진단평가 목록 페이지 요청 - 학년: {}, 학과: {}, 강의명: {}, 학생명: {}",
                grade, department, lectureName, studentName);

        List<Dept> departments = deptRepository.findAll();
        setBaseModelAttributes(model, departments, grade, department, lectureName, studentName);

        if (hasSearchCriteria(grade, department, lectureName, studentName)) {
            try {
                Long departmentId = parseDepartmentId(department);
                List<DiagQuestionDTO> results = diagQuestionService
                        .getDiagnosticResultsBySearchCriteria(
                                departmentId,
                                grade,
                                lectureName,
                                studentName
                        );

                model.addAttribute("results", results);
                model.addAttribute("showResults", !results.isEmpty());
            } catch (NumberFormatException e) {
                log.error("학과 ID 변환 중 오류 발생: {}", e.getMessage());
                model.addAttribute("error", "잘못된 학과 정보입니다.");
                model.addAttribute("showResults", false);
            }
        }

        return "view/iframe/evaluation/diag/admin/diagQuestion";
    }

    private void setBaseModelAttributes(Model model, List<Dept> departments,
                                        String grade, String department,
                                        String lectureName, String studentName) {
        model.addAttribute("departments", departments);
        model.addAttribute("showResults", false);
        model.addAttribute("selectedGrade", grade);
        model.addAttribute("selectedDepartment", department);
        model.addAttribute("selectedLectureName", lectureName);
        model.addAttribute("selectedStudentName", studentName);
    }

    private boolean hasSearchCriteria(String grade, String department,
                                      String lectureName, String studentName) {
        return grade != null || department != null ||
                !StringUtils.isEmpty(lectureName) || !StringUtils.isEmpty(studentName);
    }

    private Long parseDepartmentId(String department) {
        return department != null && !department.isEmpty() ? Long.parseLong(department) : null;
    }
}
