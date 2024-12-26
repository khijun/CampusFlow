package edu.du.campusflow.enums;

import edu.du.campusflow.annotation.Code;

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
