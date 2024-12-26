package edu.du.campusflow.enums;

import edu.du.campusflow.annotation.Code;

@Code(name = "비율", description = "성적비율이나 등급비율등 비율의 타입을 나타냄")
public enum RatioType {

    @Code(name = "성적 비율", description = "성적 항목별 비율을 나타냄")
    GRADE,

    @Code(name = "등급 비율", description = "등급 항목별 비율을 나타냄")
    GRADE_LEVEL;

    @Code(name = "성적 비율", description = "성적항목별 비율을 나타냄")
    public enum GradeRatio{

        @Code(name = "중간고사 비율", description = "중간고사 성적 비율")
        MIDTERM, //중간 성적 비율

        @Code(name = "기말고사 비율", description = "기말고사 성적 비율")
        FINAL_EXAM, //기말 성적 비율

        @Code(name = "출석 비율", description = "출석 점수 비율")
        ATTENDANCE, //출석 비율

        @Code(name = "과제 비율", description = "과제 점수 비율")
        ASSIGNMENT;  //과제 비율
    }

    @Code(name = "등급 비율", description = "등급 항목별 비율을 나타냄")
    public enum GradeLevelRatio{

        @Code(name = "A등급", description = "A등급 인원 비율")
        A, //A등급

        @Code(name = "B등급", description = "B등급 인원 비율")
        B, //B등급

        @Code(name = "C등급", description = "C등급 인원 비율")
        C, //C등급

        @Code(name = "D등급", description = "D등급 인원 비율")
        D, //D등급

        @Code(name = "F등급", description = "F등급 인원 비율")
        F; //F등급
    }

}
