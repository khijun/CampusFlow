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

}
