package edu.du.campusflow.controller;

import edu.du.campusflow.dto.DiagQuestionDTO;
import edu.du.campusflow.service.DeptService;
import edu.du.campusflow.service.DiagEvaluationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/evaluation/diag")
@RequiredArgsConstructor
public class DiagEvaluationApiController {
    private final DeptService deptService;
    private final DiagEvaluationService diagEvaluationService;

    @GetMapping("/departments")
    public List<Map<String, Object>> getDepartments() {
        return deptService.getAllDepartments().stream()
                .map(dept -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("deptId", dept.getDeptId());
                    map.put("deptName", dept.getDeptName());
                    return map;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<DiagQuestionDTO> searchDiagnosticResults(
            @RequestParam(required = false) Long deptId,
            @RequestParam(required = false) String grade,
            @RequestParam(required = false) String lectureName,
            @RequestParam(required = false) String studentName
    ) {
        return diagEvaluationService.searchDiagnosticResults(deptId, grade, lectureName, studentName);
    }
}