package edu.du.campusflow.dto;

import edu.du.campusflow.entity.DiagItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

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
            Integer score,
            String name,
            String grade,
            Long subjectId,
            Long memberId) {
        this.lectureName = lectureName;
        this.questionName = questionName;
        this.score = score;
        this.name = name;
        this.grade = grade;
        this.subjectId = subjectId;
        this.memberId = memberId;
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

    // List<DiagItem> -> List<DTO> 변환 메서드
    public static List<DiagEvaluationDetailDTO> fromList(List<DiagItem> items) {
        return items.stream()
                .map(DiagEvaluationDetailDTO::from)
                .collect(Collectors.toList());
    }

    // Object[] -> DTO 변환 메서드 (native query 결과 변환용)
    public static DiagEvaluationDetailDTO fromObject(Object[] result) {
        return DiagEvaluationDetailDTO.builder()
                .lectureName((String) result[0])
                .questionName((String) result[1])
                .score((Integer) result[2])
                .name((String) result[3])      // 추가
                .grade((String) result[4])     // 추가
                .subjectId((Long) result[5])   // 추가
                .build();
    }

    // List<Object[]> -> List<DTO> 변환 메서드
    public static List<DiagEvaluationDetailDTO> fromObjectList(List<Object[]> results) {
        return results.stream()
                .map(DiagEvaluationDetailDTO::fromObject)
                .collect(Collectors.toList());
    }

    // 점수 관련 메서드들은 그대로 유지
    public void initializeScoreCounts() {
        this.score5Count = 0;
        this.score4Count = 0;
        this.score3Count = 0;
        this.score2Count = 0;
        this.score1Count = 0;
    }

    public void incrementScoreCount(Integer score) {
        switch (score) {
            case 5: score5Count++; break;
            case 4: score4Count++; break;
            case 3: score3Count++; break;
            case 2: score2Count++; break;
            case 1: score1Count++; break;
        }
    }

    public long calculateTotalResponses() {
        return score5Count + score4Count + score3Count + score2Count + score1Count;
    }

    public void calculatePercentages() {
        long total = calculateTotalResponses();
        if (total > 0) {
            score5Percent = (double) score5Count / total * 100;
            score4Percent = (double) score4Count / total * 100;
            score3Percent = (double) score3Count / total * 100;
            score2Percent = (double) score2Count / total * 100;
            score1Percent = (double) score1Count / total * 100;
        }
    }

    public void calculateAverageScore() {
        long total = calculateTotalResponses();
        if (total > 0) {
            this.averageScore = (5 * score5Count + 4 * score4Count + 3 * score3Count +
                    2 * score2Count + score1Count) / (double) total;
        }
    }
}
