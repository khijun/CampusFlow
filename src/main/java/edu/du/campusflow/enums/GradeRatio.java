package edu.du.campusflow.enums;

import edu.du.campusflow.annotation.Code;

@Code(name = "성적 비율", description = "성적항목별 비율을 나타냄")
public enum GradeRatio{

    @Code(name = "중간고사 비율", description = "중간고사 성적 비율")
    MIDTERM_RATIO, //중간 성적 비율

    @Code(name = "기말고사 비율", description = "기말고사 성적 비율")
    FINAL_EXAM_RATIO, //기말 성적 비율

    @Code(name = "출석 비율", description = "출석 점수 비율")
    ATTENDANCE_RATIO, //출석 비율

    @Code(name = "과제 비율", description = "과제 점수 비율")
    ASSIGNMENT_RATIO;  //과제 비율
}
