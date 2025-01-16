package edu.du.campusflow.controller;

import edu.du.campusflow.dto.LecQuestionDTO;
import edu.du.campusflow.service.LecItemService;
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
    private final LecItemService lecItemService;

    @GetMapping("/admin")
    public String showAdminLectureList(Model model) {
        log.info("교직원용 강의평가 목록 조회 시작");
        try {
            List<Map<String, Object>> lectures = lecQuestionService.getAllLectures();
            log.info("조회된 강의 수: {}", lectures.size());
            model.addAttribute("lectures", lectures);
            model.addAttribute("showResults", false);
            return "view/iframe/evaluation/lec/admin/lecQuestion";
        } catch (Exception e) {
            log.error("강의평가 목록 조회 중 오류 발생", e);
            throw e;
        }
    }

    @GetMapping("/admin/{ofregistrationId}")
    public String showAdminLectureEvaluation(
            @PathVariable Long ofregistrationId,
            Model model
    ) {
        log.info("교직원용 강의평가 결과 조회 - 강의 ID: {}", ofregistrationId);

        List<Map<String, Object>> lectures = lecQuestionService.getAllLectures();
        model.addAttribute("lectures", lectures);
        model.addAttribute("selectedOfregistrationId", ofregistrationId);

        List<LecQuestionDTO> evaluationResults = lecQuestionService.getEvaluationResults(ofregistrationId);
        model.addAttribute("results", evaluationResults);
        model.addAttribute("showResults", true);

        return "view/iframe/evaluation/lec/admin/lecQuestion";
    }

    @GetMapping("/student/lecture/{ofregistrationId}")
    public String showStudentLectureEvaluation(@PathVariable Long ofregistrationId) {
        return "view/iframe/evaluation/lec/student/lecQuestion";
    }
}