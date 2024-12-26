package edu.du.campusflow.enums;

import edu.du.campusflow.annotation.Code;

@Code(name = "등급 비율", description = "등급 항목별 비율을 나타냄")
public enum GradeLevelRatio{

    @Code(name = "A등급", description = "A등급 인원 비율")
    A_RATIO, //A등급 비율

    @Code(name = "B등급", description = "B등급 인원 비율")
    B_RATIO, //B등급 비율

    @Code(name = "C등급", description = "C등급 인원 비율")
    C_RATIO, //C등급 비율

    @Code(name = "D등급", description = "D등급 인원 비율")
    D_RATIO, //D등급 비율

    @Code(name = "F등급", description = "F등급 인원 비율")
    F_RATIO; //F등급 비율
}
