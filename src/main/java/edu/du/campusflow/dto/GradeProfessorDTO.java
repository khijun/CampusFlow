package edu.du.campusflow.dto;

import edu.du.campusflow.entity.Grade;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class GradeProfessorDTO {
    private List<Long> lectureIds;  // 교수님이 담당하는 강의 ID 목록
    private Long selectedLectureId; // 선택된 강의 ID
    private String professorName;
    private String lectureName;
    private Map<String, Integer> scores;
    private String finalGrade;
    private int totalScore;
    private int subjectCredits;
    private int earnedCredits;
    private String studentName;
    private Long studentId;

    public GradeProfessorDTO(List<Long> lectureIds, Long selectedLectureId, String professorName, String lectureName,
                             Map<String, Integer> scores, String finalGrade, int totalScore,
                             int subjectCredits, int earnedCredits, String studentName, Long studentId) {
        this.lectureIds = lectureIds;
        this.selectedLectureId = selectedLectureId; // 선택된 강의 ID 할당
        this.professorName = professorName;
        this.lectureName = lectureName;
        this.scores = scores;
        this.finalGrade = finalGrade;
        this.totalScore = totalScore;
        this.subjectCredits = subjectCredits;
        this.earnedCredits = earnedCredits;
        this.studentName = studentName;
        this.studentId = studentId;
    }

}
