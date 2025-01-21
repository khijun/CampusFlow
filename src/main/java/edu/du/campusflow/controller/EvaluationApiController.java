package edu.du.campusflow.controller;

import edu.du.campusflow.dto.DiagEvaluationDetailDTO;
import edu.du.campusflow.dto.LecQuestionDTO;
import edu.du.campusflow.service.DiagEvaluationService;
import edu.du.campusflow.service.LecQuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class EvaluationApiController {

    private final DiagEvaluationService diagEvaluationService;
    private final LecQuestionService lecQuestionService;

    @GetMapping("/api/evaluation/diag/search")
    @ResponseBody
    public Map<String, Object> searchDiagEvaluations(
            @RequestParam Long deptId,
            @RequestParam String grade,
            @RequestParam(required = false) String lectureName,
            @RequestParam(required = false) String name) {

        List<DiagEvaluationDetailDTO> results = diagEvaluationService.searchEvaluations(
                deptId, grade, lectureName, name);

        return Map.of(
                "result", true,
                "data", Map.of(
                        "contents", results,
                        "pagination", Map.of(
                                "page", 1,
                                "totalCount", results.size()
                        )
                )
        );
    }

    @GetMapping("/api/evaluation/lec/search")
    @ResponseBody
    public Map<String, Object> searchLecEvaluations(
            @RequestParam Long deptId,
            @RequestParam String grade,  // gradeCodeId 대신 grade
            @RequestParam(required = false) String lectureName,
            @RequestParam(required = false) String name) {

        List<LecQuestionDTO> results = lecQuestionService.searchEvaluations(
                deptId, grade, lectureName, name);

        return Map.of(
                "result", true,
                "data", Map.of(
                        "contents", results,
                        "pagination", Map.of(
                                "page", 1,
                                "totalCount", results.size()
                        )
                )
        );
    }
}
