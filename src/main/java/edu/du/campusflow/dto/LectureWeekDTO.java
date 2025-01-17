package edu.du.campusflow.dto;

import lombok.Data;

@Data
public class LectureWeekDTO {

    private Long lectureId;          // 강의 아이디
    private String lectureName;      // 강의명
    private Integer currentStudents; // 강의 신청한 인원

    private String lectureDays;      // 강의 요일
    private Integer week;            // 강의 주차
    private String startTime;        // 시작 교시
    private String endTime;          // 종료 교시
    private String classStatus;      // 수업 상태
    private Long lectureTimeId;      // 강의 시간 아이디

    private Long facilityId;         // 강의실 아이디
    private String facilityName;     // 강의실 이름
    private String lectureWeekName;  // 강의 주차 이름
    private Long lectureWeekId;      // 강의 주차 아이디

    private String semesterName;     // 학기 코드 이름
    private String lectureStatus;    // 강의 상태
}
