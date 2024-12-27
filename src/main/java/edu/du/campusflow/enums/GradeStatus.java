package edu.du.campusflow.enums;

import edu.du.campusflow.annotation.Code;

@Code(name = "성적 유형",description = "성적에 대한 유형")
public enum GradeStatus {
    @Code(name = "중간",description = "중간고사 점수")
    MIDDLE_SCORE,
    @Code(name = "기말",description = "기말고사 점수")
    FINAL_SCORE,
    @Code(name = "과제",description = "과제 점수")
    HOMEWORK_SCORE,
    @Code(name = "출석", description = "출석 점수")
    ATTENDANCE_SCORE
}
