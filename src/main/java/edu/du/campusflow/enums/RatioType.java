package edu.du.campusflow.enums;

import edu.du.campusflow.annotation.Code;

@Code(name = "비율", description = "성적비율이나 등급비율등 비율의 타입을 나타냄")
public enum RatioType {

    @Code(name = "성적 비율", description = "성적 항목별 비율을 나타냄")
    GRADE,

    @Code(name = "등급 비율", description = "등급 항목별 비율을 나타냄")
    GRADE_LEVEL;
}
