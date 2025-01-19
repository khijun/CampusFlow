package edu.du.campusflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LecQuestionDTO {

    private String lectureName;
    private String questionName;
    private Long questionId;
    private Double score;
    private String name;
    private String grade;
    private Long subjectId;
    private Long memberId;
    private Double averageScore;

    // 각 점수별 응답 수
    private long score5Count;
    private long score4Count;
    private long score3Count;
    private long score2Count;
    private long score1Count;

    // 각 점수별 비율
    private double score5Percent;
    private double score4Percent;
    private double score3Percent;
    private double score2Percent;
    private double score1Percent;

    // JPA 쿼리용 생성자 추가
    public LecQuestionDTO(
            String lectureName,
            String questionName,
            Double score,
            String name,
            String grade,
            Long subjectId,
            Long memberId,
            long score5Count,
            long score4Count,
            long score3Count,
            long score2Count,
            long score1Count,
            double score5Percent,
            double score4Percent,
            double score3Percent,
            double score2Percent,
            double score1Percent
    ) {
        this.lectureName = lectureName;
        this.questionName = questionName;
        this.score = score;
        this.name = name;
        this.grade = grade;
        this.subjectId = subjectId;
        this.memberId = memberId;
        this.score5Count = score5Count;
        this.score4Count = score4Count;
        this.score3Count = score3Count;
        this.score2Count = score2Count;
        this.score1Count = score1Count;
        this.score5Percent = score5Percent;
        this.score4Percent = score4Percent;
        this.score3Percent = score3Percent;
        this.score2Percent = score2Percent;
        this.score1Percent = score1Percent;
    }
}