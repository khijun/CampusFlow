package edu.du.campusflow.controller;

import edu.du.campusflow.dto.GradeDTO;
import edu.du.campusflow.entity.Grade;
import edu.du.campusflow.entity.Member;
import edu.du.campusflow.repository.GradeRepository;
import edu.du.campusflow.repository.MemberRepository;
import edu.du.campusflow.service.AuthService;
import edu.du.campusflow.service.GradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class GradeController {
    private final GradeService gradeService;
    private final AuthService authService;
    private final GradeRepository gradeRepository;
    private final MemberRepository memberRepository;

    @GetMapping("/iframe/grade/student_grade")
    public String studentGrade(@RequestParam(required = false, defaultValue = "67,68,69,70") String gradeType, Model model) {
        Long memberId = authService.getCurrentMemberId();

        // gradeType 문자열을 Long 배열로 변환
        List<Long> gradeTypeList = Arrays.stream(gradeType.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        List<GradeDTO> grades = gradeService.getGroupedGradesByRole(memberId, gradeTypeList);

        model.addAttribute("grades", grades);
        return "view/iframe/grade/student_grade";
    }
}
