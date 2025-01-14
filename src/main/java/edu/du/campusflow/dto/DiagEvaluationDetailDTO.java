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
    private String lectureName;    // 강의명
    private String questionName;   // 문항명
    private Integer score;         // 점수

    // 추가 필드
    private String professorName;  // 교수명
    private String grade;       // 학년
    private Long subjectId;        // 과목코드
    private double averageScore;   // 평균 점수

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
            int score,
            String professorName,
            String grade,          // 매개변수 이름도 변경
            Long subjectId
    ) {
        this.lectureName = lectureName;
        this.questionName = questionName;
        this.score = score;
        this.professorName = professorName;
        this.grade = grade;        // 필드 이름 변경
        this.subjectId = subjectId;
    }

    // JPA Constructor 추가
    public DiagEvaluationDetailDTO(
            String lectureName,
            String questionName,
            int score,
            String professorName,
            String grade,
            Long count,
            Double avgScore
    ) {
        this.lectureName = lectureName;
        this.questionName = questionName;
        this.score = score;
        this.professorName = professorName;
        this.grade = grade;
        this.averageScore = avgScore;

        initializeScoreCounts();
        incrementScoreCount(score);
        calculatePercentages();
    }

    // DiagItem -> DTO 변환 메서드 수정
    public static DiagEvaluationDetailDTO from(DiagItem diagItem) {
        return DiagEvaluationDetailDTO.builder()
                .lectureName(diagItem.getOfRegistration().getLectureId().getLectureName())
                .questionName(diagItem.getDiagQuestion().getQuestionName())
                .score(diagItem.getScore())
                .professorName(diagItem.getOfRegistration().getLectureId().getMember().getName())
                .grade(diagItem.getOfRegistration().getLectureId().getSemester().getCodeValue())  // 학기 코드값을 학년으로 사용
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
                .build();
    }

    // List<Object[]> -> List<DTO> 변환 메서드
    public static List<DiagEvaluationDetailDTO> fromObjectList(List<Object[]> results) {
        return results.stream()
                .map(DiagEvaluationDetailDTO::fromObject)
                .collect(Collectors.toList());
    }

    // 점수 카운트 초기화
    public void initializeScoreCounts() {
        this.score5Count = 0;
        this.score4Count = 0;
        this.score3Count = 0;
        this.score2Count = 0;
        this.score1Count = 0;
    }

    // 점수별 카운트 증가
    public void incrementScoreCount(Integer score) {
        switch (score) {
            case 5: score5Count++; break;
            case 4: score4Count++; break;
            case 3: score3Count++; break;
            case 2: score2Count++; break;
            case 1: score1Count++; break;
        }
    }

    // 총 응답 수 계산
    public long calculateTotalResponses() {
        return score5Count + score4Count + score3Count + score2Count + score1Count;
    }

    // 비율 계산
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

    // 평균 점수 계산
    public void calculateAverageScore() {
        long total = calculateTotalResponses();
        if (total > 0) {
            this.averageScore = (5 * score5Count + 4 * score4Count + 3 * score3Count +
                    2 * score2Count + score1Count) / (double) total;
        }
    }


}
