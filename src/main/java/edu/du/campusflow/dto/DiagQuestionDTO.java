package edu.du.campusflow.dto;

import lombok.Data;

@Data
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

    public void initializeScoreCounts() {
        this.score5Count = 0;
        this.score4Count = 0;
        this.score3Count = 0;
        this.score2Count = 0;
        this.score1Count = 0;

        this.score5Percent = 0.0;
        this.score4Percent = 0.0;
        this.score3Percent = 0.0;
        this.score2Percent = 0.0;
        this.score1Percent = 0.0;
    }

    public void incrementScoreCount(int score) {
        switch (score) {
            case 5: score5Count++; break;
            case 4: score4Count++; break;
            case 3: score3Count++; break;
            case 2: score2Count++; break;
            case 1: score1Count++; break;
        }
    }

    public int calculateTotalResponses() {
        return score5Count + score4Count + score3Count + score2Count + score1Count;
    }

    public void calculatePercentages(int totalResponses) {
        if (totalResponses > 0) {
            score5Percent = (double) score5Count / totalResponses * 100;
            score4Percent = (double) score4Count / totalResponses * 100;
            score3Percent = (double) score3Count / totalResponses * 100;
            score2Percent = (double) score2Count / totalResponses * 100;
            score1Percent = (double) score1Count / totalResponses * 100;

            averageScore = (5.0 * score5Count + 4.0 * score4Count + 3.0 * score3Count +
                    2.0 * score2Count + 1.0 * score1Count) / totalResponses;
        }
    }
}
