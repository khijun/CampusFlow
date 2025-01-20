package edu.du.campusflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class GradeDTO {
    private String professorName;          // 교수 이름
    private String lectureName;            // 강의 이름
    private Map<String, Integer> scores;   // 성적 정보 (중간, 기말, 과제, 출석 점수 등)
    private String finalGrade;             // 등급
    private int totalScore;                // 총점
    private int subjectCredits;            // 학점 정보
    private int earnedCredits;             // 취득 학점 (미이수시 0, 그 외는 subjectCredits와 동일)
}
