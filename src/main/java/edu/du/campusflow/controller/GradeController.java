package edu.du.campusflow.controller;

import edu.du.campusflow.dto.GradeDTO;
import edu.du.campusflow.dto.GradeForm;
import edu.du.campusflow.dto.GradeProfessorDTO;
import edu.du.campusflow.entity.*;
import edu.du.campusflow.repository.*;
import edu.du.campusflow.service.AuthService;
import edu.du.campusflow.service.GradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class GradeController {
    private final GradeService gradeService;
    private final AuthService authService;
    private final GradeRepository gradeRepository;
    private final MemberRepository memberRepository;
    private final LectureRepository lectureRepository;
    private final OfregistrationRepository ofregistrationRepository;
    private final CommonCodeRepository commonCodeRepository;

    @GetMapping("/iframe/grade/student_grade")
    public String studentGrade(@RequestParam(required = false, defaultValue = "67,68,69,70") String gradeType,
                               @RequestParam(required = false) Integer academicYear,
                               Model model) {
        Long memberId = authService.getCurrentMemberId();

        List<Long> gradeTypeList = Arrays.stream(gradeType.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        List<GradeDTO> grades = gradeService.getGroupedGradesByRole(memberId, gradeTypeList);

        // 선택한 학년도만 필터링
        if (academicYear != null) {
            grades = grades.stream()
                    .filter(grade -> grade.getAcademicYear() == academicYear)
                    .collect(Collectors.toList());
        }

        // 모든 학년도 리스트 (중복 제거)
        List<Integer> academicYears = grades.stream()
                .map(GradeDTO::getAcademicYear)
                .distinct()
                .sorted(Comparator.reverseOrder()) // 최신 학년도부터 정렬
                .collect(Collectors.toList());

        model.addAttribute("grades", grades);
        model.addAttribute("academicYears", academicYears); // 학년도 리스트 추가
        model.addAttribute("selectedYear", academicYear); // 선택한 학년도 추가
        return "view/iframe/grade/student_grade";
    }

    // 모든 학생 성적 조회
    @GetMapping("/iframe/grade/professor/all_students")
    @PreAuthorize("hasAnyRole('STAFF', 'PROFESSOR')")
    public String getAllStudentGrades(Model model) {
        Long memberId = authService.getCurrentMemberId();
        try {
            // 교수가 담당하는 모든 학생의 성적 조회
            List<GradeProfessorDTO> grades = gradeService.getAllStudentGradesByProfessor(memberId, Arrays.asList(67L, 68L, 69L, 70L));

            // 성적이 부여된 강의가 없으면 에러 메시지 추가
            if (grades.isEmpty()) {
                model.addAttribute("errorMessage", "성적이 부여된 강의가 없습니다.");
            } else {
                model.addAttribute("grades", grades);
            }

            List<Long> lectureIds = lectureRepository.findLectureIdsByMemberId(memberId);
            model.addAttribute("lectureIds", lectureIds);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "학생 성적 정보를 조회하는 도중 오류가 발생했습니다.");
        }
        return "view/iframe/grade/professor/all_students"; // 모든 학생 성적을 표시할 뷰
    }
}
