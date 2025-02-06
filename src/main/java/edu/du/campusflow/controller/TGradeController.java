package edu.du.campusflow.controller;

import edu.du.campusflow.dto.GradeDTO;
import edu.du.campusflow.dto.GradeFormProfessor;
import edu.du.campusflow.dto.GradeProfessorDTO;
import edu.du.campusflow.dto.GradeUpdateDTO;
import edu.du.campusflow.service.AuthService;
import edu.du.campusflow.service.GradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
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

    @GetMapping("/api/grade/student-grade/{studentId}")
    public ResponseEntity<List<GradeProfessorDTO>> getStudentGrade(@PathVariable Long studentId) {
        Long memberId = authService.getCurrentMemberId();
        // 해당 학생의 성적을 조회
        List<GradeProfessorDTO> grades = gradeService.getStudentGradesByProfessor(
                authService.getCurrentMemberId(), studentId, Arrays.asList(67L, 68L, 69L, 70L));

        // 성적 데이터를 반환
        return ResponseEntity.ok(grades);
    }

    @GetMapping("/api/grade/student-grades")
    public ResponseEntity<List<GradeProfessorDTO>> getAllStudentGrades() {
        Long memberId = authService.getCurrentMemberId();
        List<GradeProfessorDTO> grades = gradeService.getAllStudentGradesByProfessor(
                memberId, Arrays.asList(67L, 68L, 69L, 70L));
        return ResponseEntity.ok(grades);
    }
    @PostMapping("/api/grade/update")
    public ResponseEntity<?> updateGrades(@RequestBody GradeUpdateDTO gradeUpdateDTO) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 학생별 성적 업데이트
            for (int i = 0; i < gradeUpdateDTO.getMemberIds().size(); i++) {
                Long memberId = gradeUpdateDTO.getMemberIds().get(i);
                Long lectureId = gradeUpdateDTO.getLectureIds().get(i); // 각 학생의 강의 ID 가져오기
                List<GradeFormProfessor.StudentGrade> studentGrades = createStudentGradesProfessor(
                        memberId, gradeUpdateDTO.getScores().get(i)
                );

                GradeFormProfessor gradeFormProfessor = new GradeFormProfessor(lectureId, studentGrades);
                gradeService.updateGrade(gradeFormProfessor);
            }

            response.put("message", "성적이 성공적으로 수정되었습니다.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("❌ 성적 업데이트 중 오류 발생: ", e);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 교수 성적 리스트 생성
    // 교수 성적 리스트 생성
    private List<GradeFormProfessor.StudentGrade> createStudentGradesProfessor(Long memberId, Map<String, Integer> scores) {
        log.info("📌 createStudentGradesProfessor 호출됨 - memberId: {}", memberId);
        log.info("📌 scores: {}", scores);

        if (scores == null || scores.isEmpty()) {
            log.error("❌ 성적 데이터가 비어 있음 - memberId: {}", memberId);
            throw new IllegalArgumentException("성적 데이터가 비어 있습니다.");
        }

        List<GradeFormProfessor.StudentGrade> studentGrades = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            String gradeType = entry.getKey();
            Integer score = entry.getValue();

            log.info("✅ 추가 중 - memberId: {}, gradeType: {}, score: {}", memberId, gradeType, score);
            studentGrades.add(new GradeFormProfessor.StudentGrade(memberId, gradeType, score));
        }
        return studentGrades;
    }
}
