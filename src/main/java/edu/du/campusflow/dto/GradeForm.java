package edu.du.campusflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GradeForm {
    private Long lectureId; // 강의 ID
    private List<StudentGrade> studentGrades; // 학생별 성적 목록

    @Data
    @AllArgsConstructor
    public static class StudentGrade {
        private Long memberId; // 학생 ID
        private String gradeType; // 성적 유형
        private int score; // 점수
    }
}