package edu.du.campusflow.dto;

import lombok.Data;

@Data
public class LectureTimeDTO {

    private Long lectureTimeId;      //강의 시간 아이디
    private String lectureWeek;      //강의 주차 아이디
    private String lectureName;      //강의 명
    private String startTime;        //시작 교시
    private String endTime;          //종료 교시
    private String lectureDay;       //강의 요일
    private String professorName;    //교수 이름
}
