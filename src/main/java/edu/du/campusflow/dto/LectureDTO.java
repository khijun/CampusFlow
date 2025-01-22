package edu.du.campusflow.dto;

import lombok.Data;

@Data
public class LectureDTO {

    private String lectureName; //강의명
    private String professorName;
    private Integer maxStudents; //강의 최대 인원
    private Integer currentStudents; //강의 신청한 인원
    private Long curriculumSubjectId; // 선택한 교과목 ID
    private String semesterName; // 학기 코드 이름
    private String lectureStatus; //강의 상태

    private Integer subjectCredits; //학점


    private String lectureDays;      // 강의 요일
    private Integer week;            // 강의 주차
    private String startTime;        // 시작 교시
    private String endTime;          // 종료 교시

    private String deptName;         //학과명
    private Long lectureId;          //강의 아이디
    private Long facilityId;         //강의실 아이디
    private String facilityName;     //강의실 이름

    private String semesterCode;

    private Long FileInfo;           //저장된 파일 아이디

}
