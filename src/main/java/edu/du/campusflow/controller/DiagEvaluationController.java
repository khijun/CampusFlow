package edu.du.campusflow.controller;

import edu.du.campusflow.dto.DiagEvaluationDetailDTO;
import edu.du.campusflow.dto.DiagQuestionDTO;
import edu.du.campusflow.entity.Member;
import edu.du.campusflow.service.AuthService;
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
    private final AuthService authService;

    // 관리자용 진단평가 페이지 이동
    @GetMapping("/admin")
    public String showAdminDiagnosticList() {
        return "view/iframe/evaluation/diag/admin/adminDiag";
    }

    // 관리자용 학과 목록 조회
    @GetMapping("/admin/departments")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getDepartments() {
        return ResponseEntity.ok(diagEvaluationService.getAllDepartments());
    }

    // 관리자용 학과/학년 검색
    @GetMapping("/admin/search")
    @ResponseBody
    public ResponseEntity<List<DiagEvaluationDetailDTO>> searchEvaluations(
            @RequestParam Long deptId,
            @RequestParam String grade,
            @RequestParam(required = false) String lectureName,
            @RequestParam(required = false) String name) {

        log.info("Search params - deptId: {}, grade: {}, lectureName: {}, name: {}",
                deptId, grade, lectureName, name);

        List<DiagEvaluationDetailDTO> results = diagEvaluationService.searchEvaluations(
                deptId, grade, lectureName, name);

        log.info("Search results size: {}", results.size());
        return ResponseEntity.ok(results);
    }


    // 교수용 진단평가 페이지 이동
    @GetMapping("/professor")
    public String showProfessorDiagnosticList() {
        return "view/iframe/evaluation/diag/professor/professorDiag";
    }

    // 교수의 학과 정보 조회
    @GetMapping("/professor/departments")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getProfessorDepartment() {
        return ResponseEntity.ok(diagEvaluationService.getProfessorDepartment());
    }

    // 교수용 학과/학년 검색
    @GetMapping("/professor/search")
    @ResponseBody
    public ResponseEntity<List<DiagEvaluationDetailDTO>> searchProfessorEvaluations(
            @RequestParam Long deptId,
            @RequestParam String grade,
            @RequestParam(required = false) String lectureName,
            @RequestParam(required = false) String name) {

        log.info("Professor Search params - deptId: {}, grade: {}, lectureName: {}, name: {}",
                deptId, grade, lectureName, name);

        List<DiagEvaluationDetailDTO> results = diagEvaluationService.searchEvaluations(
                deptId, grade, lectureName, name);

        log.info("Professor Search results size: {}", results.size());
        return ResponseEntity.ok(results);
    }


    // 학생용 진단평가 페이지 이동
    @GetMapping("/student")
    public String showStudentDiagList(Model model) {
        Member currentStudent = authService.getCurrentMember();
        log.info("Student diag list - studentId: {}", currentStudent.getMemberId());

        List<Map<String, Object>> lectures = diagEvaluationService.getStudentLecturesWithEvalStatus(
                currentStudent.getMemberId());
        model.addAttribute("lectures", lectures);

        return "studentDiagForm";
    }

    @GetMapping("/student/{ofregistrationId}")
    public String showStudentDiagForm(
            @PathVariable Long ofregistrationId,
            Model model
    ) {
        log.info("Student diag form - ofregistrationId: {}", ofregistrationId);

        // 현재 로그인한 학생 정보 가져오기
        Member currentStudent = authService.getCurrentMember();

        // 이미 평가를 완료했는지 확인
        boolean evaluated = diagEvaluationService.isAlreadyEvaluated(ofregistrationId);
        if (evaluated) {
            return "redirect:/iframe/evaluation/diag/student";
        }

        // 학생의 수강 강의 목록 조회
        List<LectureDTO> lectures = diagEvaluationService.getStudentLectures(currentStudent.getMemberId());

        // 진단평가 문항 조회
        List<DiagQuestionDTO> questions = diagEvaluationService.getDiagQuestions(ofregistrationId);

        model.addAttribute("lectures", lectures);
        model.addAttribute("questions", questions);
        model.addAttribute("evaluated", evaluated);
        model.addAttribute("showForm", ofregistrationId != null);
        model.addAttribute("selectedOfregistrationId", ofregistrationId);

        return "evaluation/diag/studentDiagForm";
    }

    @GetMapping("/student/lectures")
    public ResponseEntity<List<Map<String, Object>>> getStudentLectures() {
        Member currentStudent = authService.getCurrentMember();
        List<Map<String, Object>> result =
                diagEvaluationService.getStudentLecturesWithEvalStatus(currentStudent.getMemberId());
        return ResponseEntity.ok(result);
    }
}