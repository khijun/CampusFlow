package edu.du.campusflow.dto;

import lombok.Data;

@Data
public class LectureDTO {

    private String lectureName;
    private String professorName;
    private Integer maxStudents;
    private Long curriculumSubjectId; // 선택한 교과목 ID
    private String semesterCodeName; // 학기 코드 이름

}
