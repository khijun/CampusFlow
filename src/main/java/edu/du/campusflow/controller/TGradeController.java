package edu.du.campusflow.controller;

import edu.du.campusflow.dto.GradeDTO;
import edu.du.campusflow.service.AuthService;
import edu.du.campusflow.service.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class TGradeController {


    private final AuthService authService;
    private final GradeService gradeService;

    @GetMapping("/api/grade/student-grade")
    public ResponseEntity<List<GradeDTO>> getStudentGrade(@RequestParam(required = false, defaultValue = "67,68,69,70") String gradeType) {
        Long memberId = authService.getCurrentMemberId();

        // gradeType 문자열을 Long 배열로 변환
        List<Long> gradeTypeList = Arrays.stream(gradeType.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        List<GradeDTO> grades = gradeService.getGroupedGradesByRole(memberId, gradeTypeList);

        return ResponseEntity.ok(grades);
    }
}
