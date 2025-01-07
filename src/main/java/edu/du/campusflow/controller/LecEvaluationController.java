package edu.du.campusflow.controller;

import edu.du.campusflow.dto.LecQuestionDTO;
import edu.du.campusflow.service.LecQuestionService;
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
@RequestMapping("/iframe/evaluation/lec")
@RequiredArgsConstructor
public class LecEvaluationController {

    private final LecQuestionService lecQuestionService;

    // 교수용 강의평가 결과 조회 - 강의 목록 페이지
    @GetMapping("/professor")
    public String showProfessorLectureList(Model model) {
        log.info("교수용 강의평가 목록 조회");
        List<Map<String, Object>> lectures = lecQuestionService.getProfessorLectures();
        model.addAttribute("lectures", lectures);
        // 초기에는 결과를 보여주지 않음
        model.addAttribute("showResults", false);
        return "view/iframe/evaluation/lec/professor/lecQuestion";
    }

    // 특정 강의의 평가 결과 조회
    @GetMapping("/professor/{ofregistrationId}")
    public String showProfessorLectureEvaluation(
            @PathVariable Long ofregistrationId,
            Model model
    ) {
        log.info("교수용 강의평가 결과 조회 - 강의 ID: {}", ofregistrationId);

        // 강의 목록 조회
        List<Map<String, Object>> lectures = lecQuestionService.getProfessorLectures();
        model.addAttribute("lectures", lectures);
        model.addAttribute("selectedOfregistrationId", ofregistrationId);

        // 선택된 강의의 평가 결과 조회
        List<LecQuestionDTO> evaluationResults = lecQuestionService.getEvaluationResults(ofregistrationId);
        model.addAttribute("results", evaluationResults);
        model.addAttribute("showResults", true);  // 결과 표시 플래그

        return "view/iframe/evaluation/lec/professor/lecQuestion";
    }

    @GetMapping("/admin")
    public String showAdminLectureList(Model model) {
        log.info("교직원용 강의평가 목록 조회 시작");  // 로그 추가
        try {
            List<Map<String, Object>> lectures = lecQuestionService.getAllLectures();
            log.info("조회된 강의 수: {}", lectures.size());  // 로그 추가
            model.addAttribute("lectures", lectures);
            model.addAttribute("showResults", false);
            return "view/iframe/evaluation/lec/admin/lecQuestion";
        } catch (Exception e) {
            log.error("강의평가 목록 조회 중 오류 발생", e);  // 에러 로그 추가
            throw e;
        }
    }

    @GetMapping("/admin/{ofregistrationId}")
    public String showAdminLectureEvaluation(
            @PathVariable Long ofregistrationId,
            Model model
    ) {
        log.info("교직원용 강의평가 결과 조회 - 강의 ID: {}", ofregistrationId);

        // 모든 강의 목록 조회
        List<Map<String, Object>> lectures = lecQuestionService.getAllLectures();
        model.addAttribute("lectures", lectures);
        model.addAttribute("selectedOfregistrationId", ofregistrationId);

        // 선택된 강의의 평가 결과 조회
        List<LecQuestionDTO> evaluationResults = lecQuestionService.getEvaluationResults(ofregistrationId);
        model.addAttribute("results", evaluationResults);
        model.addAttribute("showResults", true);

        return "view/iframe/evaluation/lec/admin/lecQuestion";
    }

    // 학생용 강의평가 페이지
    @GetMapping("/student/lecture/{ofregistrationId}")
    public String showStudentLectureEvaluation(@PathVariable Long ofregistrationId) {
        return "view/iframe/evaluation/lec/student/lecQuestion";
    }
}