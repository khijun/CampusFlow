package edu.du.campusflow.dto;

import edu.du.campusflow.entity.DiagItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiagEvaluationDetailDTO {
    private String lectureName;
    private String questionName;
    private Long questionId;
    private Integer score;
    private String name;
    private String grade;
    private Long subjectId;
    private Long memberId;
    private Double averageScore;

    // 점수별 응답 수
    private long score5Count;
    private long score4Count;
    private long score3Count;
    private long score2Count;
    private long score1Count;

    // 점수별 비율
    private double score5Percent;
    private double score4Percent;
    private double score3Percent;
    private double score2Percent;
    private double score1Percent;

    public DiagEvaluationDetailDTO(
            String lectureName,
            String questionName,
            Long questionId,
            Integer score,
            String name,
            String grade,
            Long subjectId,
            Long memberId,
            Double averageScore) {
        this.lectureName = lectureName;
        this.questionName = questionName;
        this.questionId = questionId;
        this.score = score;
        this.name = name;
        this.grade = grade;
        this.subjectId = subjectId;
        this.memberId = memberId;
        this.averageScore = averageScore;
    }

    // DiagItem -> DTO 변환 메서드 수정
    public static DiagEvaluationDetailDTO from(DiagItem diagItem) {
        return DiagEvaluationDetailDTO.builder()
                .lectureName(diagItem.getOfRegistration().getLectureId().getLectureName())
                .questionName(diagItem.getDiagQuestion().getQuestionName())
                .score(diagItem.getScore())
                .name(diagItem.getOfRegistration().getMember().getName())
                .grade(diagItem.getOfRegistration().getMember().getGrade().getCodeValue())  // CommonCode에서 값 가져오기
                .subjectId(diagItem.getOfRegistration().getLectureId().getCurriculumSubject().getSubject().getSubjectId())
                .build();
    }

}
