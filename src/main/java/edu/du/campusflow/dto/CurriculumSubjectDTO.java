package edu.du.campusflow.dto;

import lombok.Data;

@Data
public class CurriculumSubjectDTO {

    private Long curriculumSubjectId; // 추가: 교육과정 교과목 ID
    private String subjectName;
    private String curriculumName;
    private String subjectCredits;
    private String semesterName;
    private String subjectTypeName; // 코드 이름 추가

    private String deptName;             //학과 명
    private Integer curriculumYear;      //연도
}
