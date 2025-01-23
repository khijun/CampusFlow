package edu.du.campusflow.controller;

import edu.du.campusflow.dto.GradeDTO;
import edu.du.campusflow.dto.GradeForm;
import edu.du.campusflow.dto.GradeFormProfessor;
import edu.du.campusflow.dto.GradeProfessorDTO;
import edu.du.campusflow.entity.*;
import edu.du.campusflow.repository.*;
import edu.du.campusflow.service.AuthService;
import edu.du.campusflow.service.GradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    @GetMapping("/iframe/grade/professor/professor_view")
    public String showAssignGradeForm(Model model) {
        Long memberId = authService.getCurrentMemberId(); // 현재 로그인된 교수의 ID





        // 교수님이 맡은 강의 ID 목록 조회
        List<Long> lectureIds = lectureRepository.findLectureIdsByMemberId(memberId);
        model.addAttribute("lectureIds", lectureIds); // 강의 ID 목록

        // 강의 ID에 해당하는 강의 정보 조회
        List<Lecture> lectures = lectureRepository.findAllById(lectureIds);
        model.addAttribute("lectures", lectures); // 강의 목록

        // 성적 유형 필터링: 중간, 기말, 과제, 출석
        List<CommonCode> gradeTypes = commonCodeRepository.findAllById(Arrays.asList(67L, 68L, 69L, 70L));
        model.addAttribute("gradeTypes", gradeTypes); // 성적 유형

        List<Ofregistration> ofregistrations = ofregistrationRepository.findByLectureId_LectureId(memberId);
        model.addAttribute("ofregistrations", ofregistrations);



        return "view/iframe/grade/professor/professor_view"; // 학생과 강의 리스트를 표시할 뷰 이름
    }

    @GetMapping("/iframe/grade/professor/get_students")
    public String getStudentsByLectureId(@RequestParam Long lectureId, Model model) {
        // 선택한 강의 ID에 해당하는 수강 신청 학생 목록 조회
        List<Ofregistration> ofregistrations = ofregistrationRepository.findByLectureId(lectureId);

        model.addAttribute("ofregistrations", ofregistrations);  // 학생 목록 모델에 추가

// 성적 유형 필터링: 중간, 기말, 과제, 출석
        List<CommonCode> gradeType= commonCodeRepository.findAllById(Arrays.asList(67L, 68L, 69L, 70L));
        model.addAttribute("gradeType", gradeType); // 성적 유

        // 현재 강의 ID도 모델에 담아서 전달
        model.addAttribute("lectureId", lectureId); // 예를 들어, 첫 번째 강의 ID를 보내기
        // 학생 목록을 포함한 뷰를 반환
        return "view/iframe/grade/professor/_student_list :: studentList";  // 템플릿 조각 반환
    }

    @PostMapping("/iframe/grade/professor/assign")
    public String assignGrade(@RequestParam("lectureId") Long lectureId,
                              @RequestParam List<Long> memberId,
                              @RequestParam List<String> gradeType,
                              @RequestParam List<Integer> score,
                              RedirectAttributes redirectAttributes) {
        try {
            // GradeForm을 생성하여 서비스를 호출
            GradeForm gradeForm = new GradeForm(lectureId, createStudentGrades(memberId, gradeType, score));
            gradeService.assignGrade(gradeForm);

            // 성공 메시지
            redirectAttributes.addFlashAttribute("message", "성적이 성공적으로 부여되었습니다.");
        } catch (IllegalArgumentException e) {
            // 요청 오류 처리
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (IllegalStateException e) {
            // 상태 불일치 처리
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/iframe/grade/professor/professor_view"; // 리다이렉트 경로
    }

    // 학생 성적 조회
    @GetMapping("/iframe/grade/professor/student_grade/{studentId}")
    public String getStudentGrade(@PathVariable Long studentId, Model model) {
        Long memberId = authService.getCurrentMemberId();
        // 해당 학생의 성적을 조회
        List<GradeProfessorDTO> grades = gradeService.getStudentGradesByProfessor(authService.getCurrentMemberId(), studentId, Arrays.asList(67L, 68L, 69L, 70L));
        List<Long> lectureIds = lectureRepository.findLectureIdsByMemberId(memberId);
        model.addAttribute("grades", grades);
        model.addAttribute("studentId", studentId);
        model.addAttribute("lectureIds", lectureIds);
        return "view/iframe/grade/professor/student_grade"; // 학생 성적을 표시할 뷰
    }



    @PostMapping("/iframe/grade/professor/edit/{memberId}")
    public String updateGrade(@PathVariable Long memberId, @RequestParam Long lectureId,
                              @RequestParam List<String> gradeType, @RequestParam List<Integer> score,
                              @RequestParam Long selectedLectureId,  // 선택된 강의 ID 추가
                              RedirectAttributes redirectAttributes) {
        try {
            // GradeForm 생성 시 lectureId와 selectedLectureId를 사용하여 생성
            GradeFormProfessor gradeFormProfessor = new GradeFormProfessor(lectureId, selectedLectureId, createStudentGradesProfessor(Arrays.asList(memberId), gradeType, score));
            gradeService.updateGrade(gradeFormProfessor);

            redirectAttributes.addFlashAttribute("message", "성적이 성공적으로 수정되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/iframe/grade/professor/professor_view"; // 성적 수정 후 교수 뷰로 리다이렉트
    }

    // 학생 성적 리스트 생성
    private List<GradeForm.StudentGrade> createStudentGrades(List<Long> memberIds, List<String> gradeTypes, List<Integer> scores) {
        List<GradeForm.StudentGrade> studentGrades = new ArrayList<>();
        for (int i = 0; i < memberIds.size(); i++) {
            studentGrades.add(new GradeForm.StudentGrade(memberIds.get(i), gradeTypes.get(i), scores.get(i)));
        }
        return studentGrades;
    }


    // 교수 성적 리스트 생성
    private List<GradeFormProfessor.StudentGrade> createStudentGradesProfessor(List<Long> memberIds, List<String> gradeTypes, List<Integer> scores) {
        List<GradeFormProfessor.StudentGrade> studentGrades = new ArrayList<>();
        for (int i = 0; i < memberIds.size(); i++) {
            studentGrades.add(new GradeFormProfessor.StudentGrade(memberIds.get(i), gradeTypes.get(i), scores.get(i)));
        }
        return studentGrades;
    }

}
