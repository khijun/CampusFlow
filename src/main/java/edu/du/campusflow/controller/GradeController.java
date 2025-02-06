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

    @GetMapping("/iframe/grade/professor/professor_view")
    @PreAuthorize("hasAnyRole('STAFF', 'PROFESSOR')")
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
    @PreAuthorize("hasAnyRole('STAFF', 'PROFESSOR')")
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
    @PreAuthorize("hasAnyRole('STAFF', 'PROFESSOR')")
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
    @PreAuthorize("hasAnyRole('STAFF', 'PROFESSOR')")
    public String getStudentGrade(@PathVariable Long studentId, Model model) {
        Long memberId = authService.getCurrentMemberId();
        try {
            // 해당 학생의 성적을 조회
            List<GradeProfessorDTO> grades = gradeService.getStudentGradesByProfessor(authService.getCurrentMemberId(), studentId, Arrays.asList(67L, 68L, 69L, 70L));

            // 성적이 부여된 강의가 없으면 에러 메시지 추가
            if (grades.isEmpty()) {
                model.addAttribute("errorMessage", "성적이 부여된 강의가 없습니다.");
            } else {
                model.addAttribute("grades", grades);
            }

            List<Long> lectureIds = lectureRepository.findLectureIdsByMemberId(memberId);
            model.addAttribute("studentId", studentId);
            model.addAttribute("lectureIds", lectureIds);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "학생 성적 정보를 조회하는 도중 오류가 발생했습니다.");
        }
        return "all_students"; // 학생 성적을 표시할 뷰
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



//    @PostMapping("/iframe/grade/professor/edit")
//    @ResponseBody
//    public ResponseEntity<Map<String, Object>> updateGrades(@RequestParam List<Long> memberIds,
//                                                            @RequestParam Long lectureId,
//                                                            @RequestParam List<String> gradeType,
//                                                            @RequestParam List<Integer> score) {
//        Map<String, Object> response = new HashMap<>();
//
//        try {
//            // 여러 학생의 성적을 수정하기 위해 반복문 사용
//            for (Long memberId : memberIds) {
//                GradeFormProfessor gradeFormProfessor = new GradeFormProfessor(
//                        lectureId,
//                        createStudentGradesProfessor(Arrays.asList(memberId), score)
//                );
//                gradeService.updateGrade(gradeFormProfessor); // 각 학생의 성적을 업데이트
//            }
//
//            response.put("message", "성적이 성공적으로 수정되었습니다.");
//            return ResponseEntity.ok(response);  // HTTP 200 응답으로 메시지 반환
//        } catch (Exception e) {
//            response.put("error", e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);  // HTTP 500 응답으로 오류 메시지 반환
//        }
//    }

    // 학생 성적 리스트 생성
    private List<GradeForm.StudentGrade> createStudentGrades(List<Long> memberIds, List<String> gradeTypes, List<Integer> scores) {
        List<GradeForm.StudentGrade> studentGrades = new ArrayList<>();
        for (int i = 0; i < memberIds.size(); i++) {
            studentGrades.add(new GradeForm.StudentGrade(memberIds.get(i), gradeTypes.get(i), scores.get(i)));
        }
        return studentGrades;
    }


    // 교수 성적 리스트 생성
//    private List<GradeFormProfessor.StudentGrade> createStudentGradesProfessor(List<Long> memberIds, List<Integer> scores) {
//        List<GradeFormProfessor.StudentGrade> studentGrades = new ArrayList<>();
//        for (int i = 0; i < memberIds.size(); i++) {
//            studentGrades.add(new GradeFormProfessor.StudentGrade(memberIds.get(i),scores.get(i)));
//        }
//        return studentGrades;
//    }

}
