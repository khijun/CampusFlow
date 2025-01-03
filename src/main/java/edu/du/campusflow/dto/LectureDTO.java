package edu.du.campusflow.dto;

import lombok.Data;

@Data
public class LectureDTO {

    private Long lectureId;
    private String lectureName;
    private String professorName; // 교수 이름
    private String semesterCodeName; // 학기 코드 이름
    private Integer maxStudents;
    private Integer subjectCredits; // 과목 학점

}
