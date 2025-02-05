package edu.du.campusflow.controller;

import edu.du.campusflow.dto.LecQuestionDTO;
import edu.du.campusflow.dto.LectureDTO;
import edu.du.campusflow.entity.Lecture;
import edu.du.campusflow.entity.Member;
import edu.du.campusflow.service.AuthService;
import edu.du.campusflow.service.DiagEvaluationService;
import edu.du.campusflow.service.LecQuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/iframe/evaluation/lec")
@RequiredArgsConstructor
public class LecEvaluationController {

    private final LecQuestionService lecQuestionService;
    private final DiagEvaluationService diagEvaluationService;
    private final AuthService authService;

    // 교직원용 강의평가 페이지 이동
    @GetMapping("/admin")
    @PreAuthorize("hasAnyRole('STAFF')")
    public String showAdminLecList() {
        return "view/iframe/evaluation/lec/admin/adminLec";
    }

    // 관리자용 학과 목록 조회
    @GetMapping("/admin/departments")
    @PreAuthorize("hasAnyRole('STAFF')")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getDepartments() {
        return ResponseEntity.ok(lecQuestionService.getAllDepartments());
    }

    // 관리자용 학과/학년 검색
    @GetMapping("/admin/search")
    @PreAuthorize("hasAnyRole('STAFF')")
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

    // 학과 선택에 따른 과목 선택
    @GetMapping("/admin/lectures")
    @PreAuthorize("hasAnyRole('STAFF')")
    @ResponseBody
    public ResponseEntity<List<LectureDTO>> getLecturesByDepartment(@RequestParam Long deptId) {
        List<Lecture> lectures = lecQuestionService.getLecLecturesByDepartment(deptId);

        List<LectureDTO> lectureDTOs = lectures.stream()
                .map(lecture -> {
                    LectureDTO dto = new LectureDTO();
                    dto.setLectureName(lecture.getLectureName());
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(lectureDTOs);
    }

    // 교수용 강의평가 페이지 이동
    @GetMapping("/professor")
    @PreAuthorize("hasAnyRole('STAFF', 'PROFESSOR')")
    public String showProfessorLecturesList(Model model) {
        Member professor = authService.getCurrentMember();  // 현재 로그인한 교수 정보 가져오기

        // 교수의 강의 목록 조회
        List<Map<String, Object>> lectures = lecQuestionService.getProfessorLectures(professor.getMemberId());
        model.addAttribute("lectures", lectures);

        return "view/iframe/evaluation/lec/professor/professorLec";
    }

    // 교수의 학과 정보 조회
    @GetMapping("/professor/departments")
    @PreAuthorize("hasAnyRole('STAFF', 'PROFESSOR')")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getProfessorDepartment() {
        log.info("Getting professor department info");  // 로그 추가
        return ResponseEntity.ok(diagEvaluationService.getProfessorDepartment());
    }

    // 교수용 학과/학년 검색
    @GetMapping("/professor/search")
    @PreAuthorize("hasAnyRole('STAFF', 'PROFESSOR')")
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


    // 학생용 강의평가 페이지 이동
    @GetMapping("/student")
    public String showStudentLecList(Model model) {
        Member currentStudent = authService.getCurrentMember();
        log.info("Student lec list - studentId: {}", currentStudent.getMemberId());

        List<Map<String, Object>> lectures = lecQuestionService.getStudentLecturesWithEvalStatus(
                currentStudent.getMemberId());

        log.info("조회된 강의 목록 크기: {}", lectures.size());
        log.info("조회된 강의 목록: {}", lectures);

        model.addAttribute("lectures", lectures);
        model.addAttribute("showForm", false);

        return "view/iframe/evaluation/lec/student/studentLecForm";
    }

    @GetMapping("/student/lectures")
    public ResponseEntity<List<Map<String, Object>>> getStudentLectures() {
        Member currentStudent = authService.getCurrentMember();
        List<Map<String, Object>> result =
                lecQuestionService.getStudentLecturesWithEvalStatus(currentStudent.getMemberId());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/student/{ofregistrationId}")
    public String showStudentLecForm(
            @PathVariable Long ofregistrationId,
            Model model
    ) {
        log.info("Student lec form - ofregistrationId: {}", ofregistrationId);

        Member currentStudent = authService.getCurrentMember();

        // 강의평가 완료 여부 확인
        boolean evaluated = lecQuestionService.isAlreadyEvaluated(ofregistrationId);

        // 강의평가 문항 목록 조회
        List<LecQuestionDTO> questions = lecQuestionService.getLecQuestions(ofregistrationId);
        model.addAttribute("questions", questions);

        // 기존 답변 조회 (evaluated가 true일 때만)
        Map<Long, Integer> previousAnswers = null;
        if (evaluated) {
            previousAnswers = lecQuestionService.getPreviousAnswers(ofregistrationId);
            model.addAttribute("previousAnswers", previousAnswers);
        }

        List<Map<String, Object>> lectures =
                lecQuestionService.getStudentLecturesWithEvalStatus(currentStudent.getMemberId());

        model.addAttribute("lectures", lectures);
        model.addAttribute("evaluated", evaluated);
        model.addAttribute("showForm", ofregistrationId != null);
        model.addAttribute("selectedOfregistrationId", ofregistrationId);

        return "view/iframe/evaluation/lec/student/studentLecForm";
    }

    @PostMapping("/student/submit")
    public String submitLecEvaluation(
            @RequestParam Long id,
            @RequestParam Map<String, String> scores,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        log.info("=== Request Info ===");
        log.info("Method: {}", request.getMethod());
        log.info("ID: {}", id);
        log.info("Scores: {}", scores);
        log.info("All Parameters: {}", request.getParameterMap());

        try {
            if (id == null) {
                String ofregId = scores.get("ofregistrationId");
                if (ofregId != null) {
                    id = Long.parseLong(ofregId);
                } else {
                    throw new IllegalArgumentException("Missing ofregistration ID");
                }
            }

            Map<Long, Integer> convertedScores = scores.entrySet().stream()
                    .filter(entry -> entry.getKey().startsWith("scores["))
                    .collect(Collectors.toMap(
                            entry -> Long.parseLong(entry.getKey().replace("scores[", "").replace("]", "")),
                            entry -> Integer.parseInt(entry.getValue())
                    ));

            lecQuestionService.saveLecEvaluation(id, convertedScores);
            redirectAttributes.addFlashAttribute("message", "강의평가가 성공적으로 제출되었습니다.");
        } catch (Exception e) {
            log.error("강의평가 제출 중 오류 발생", e);
            redirectAttributes.addFlashAttribute("error", "강의평가 제출 중 오류가 발생했습니다.");
        }

        return "redirect:/iframe/evaluation/lec/student/" + id;
    }

}