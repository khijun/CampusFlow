package edu.du.campusflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiagQuestionDTO {
    private Long questionId;
    private String questionName;
    private String lectureName;
    private String name;
    private String semester;
    private Long subjectId;
    private double averageScore;

    private int score5Count;
    private int score4Count;
    private int score3Count;
    private int score2Count;
    private int score1Count;

    private double score5Percent;
    private double score4Percent;
    private double score3Percent;
    private double score2Percent;
    private double score1Percent;

    // 점수 초기화
    public void initializeScoreCounts() {
        this.score5Count = 0;
        this.score4Count = 0;
        this.score3Count = 0;
        this.score2Count = 0;
        this.score1Count = 0;
    }

    // 점수 증가
    public void incrementScoreCount(Integer score) {
        switch (score) {
            case 5: score5Count++; break;
            case 4: score4Count++; break;
            case 3: score3Count++; break;
            case 2: score2Count++; break;
            case 1: score1Count++; break;
        }
    }

    // 평균 점수 계산
    public void calculateAverageScore() {
        long totalCount = score5Count + score4Count + score3Count + score2Count + score1Count;
        if (totalCount == 0) {
            this.averageScore = 0.0;
            return;
        }

        double totalScore = (5 * score5Count) + (4 * score4Count) + (3 * score3Count) +
                (2 * score2Count) + score1Count;
        this.averageScore = totalScore / totalCount;
    }
}
