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
}
