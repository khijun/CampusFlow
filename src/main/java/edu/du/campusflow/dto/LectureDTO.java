package edu.du.campusflow.dto;

import lombok.Data;

@Data
public class LectureDTO {

    private String lectureName; //강의명
    private String professorName;
    private Integer maxStudents; //강의 최대 인원
    private Long curriculumSubjectId; // 선택한 교과목 ID
    private String semesterCodeName; // 학기 코드 이름
    private String lectureStatus; //강의 상태

}
