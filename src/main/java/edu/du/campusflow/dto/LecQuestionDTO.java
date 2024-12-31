package edu.du.campusflow.dto;

import lombok.Data;

@Data
public class LecQuestionDTO {
    private Long questionId;
    private String questionName;
    private double averageScore;

    // 각 점수별 응답 수
    private Long score5Count;
    private Long score4Count;
    private Long score3Count;
    private Long score2Count;
    private Long score1Count;

    // 각 점수별 백분율
    private double score5Percent;
    private double score4Percent;
    private double score3Percent;
    private double score2Percent;
    private double score1Percent;
}
