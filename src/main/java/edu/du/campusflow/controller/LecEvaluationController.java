package edu.du.campusflow.controller;

import edu.du.campusflow.service.LecItemService;
import edu.du.campusflow.service.LecQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/iframe/evaluation")
@RequiredArgsConstructor
public class LecEvaluationController {

    private final LecQuestionService lecQuestionService;
    private final LecItemService lecItemService;

    // 교수용 강의평가 결과 페이지
    @GetMapping("/professor/lecture/{ofregistrationId}")
    public String showProfessorLectureEvaluation(@PathVariable Long ofregistrationId) {
        return "view/iframe/evaluation/professor/lecQuestion";
    }

    // 학생용 강의평가 페이지
    @GetMapping("/student/lecture/{ofregistrationId}")
    public String showStudentLectureEvaluation(@PathVariable Long ofregistrationId) {
        return "view/iframe/evaluation/student/lecQuestion";
    }

    // 강의평가 결과 페이지
    @GetMapping("/result/{ofregistrationId}")
    public String showEvaluationResult(@PathVariable Long ofregistrationId) {
        return "view/iframe/evaluation/lecResult";
    }
}