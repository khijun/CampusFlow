package edu.du.campusflow.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DiagEvaluationStatsDTO {
    private Long subjectId;
    private Long semester;
    private String lectureName;
    private Long count;
    private Double average;

    public DiagEvaluationStatsDTO(Long subjectId, Long semester, String lectureName, Long count, Double average) {
        this.subjectId = subjectId;
        this.semester = semester;
        this.lectureName = lectureName;
        this.count = count;
        this.average = average;
    }
}
