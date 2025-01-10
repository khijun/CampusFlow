package edu.du.campusflow.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DiagEvaluationDetailDTO {
    private String lectureName;
    private String questionName;
    private Integer score;
    private Long subjectId;
    private Long semester;
    private String name;

    // 첫 번째 쿼리용 생성자
    public DiagEvaluationDetailDTO(String lectureName, String questionName, Integer score) {
        this.lectureName = lectureName;
        this.questionName = questionName;
        this.score = score;
    }

    // 두 번째 쿼리용 생성자
    public DiagEvaluationDetailDTO(Long subjectId, Long semester, String lectureName, String name) {
        this.subjectId = subjectId;
        this.semester = semester;
        this.lectureName = lectureName;
        this.name = name;
    }
}
