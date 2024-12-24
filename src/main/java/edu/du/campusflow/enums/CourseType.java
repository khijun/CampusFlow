package edu.du.campusflow.enums;

import edu.du.campusflow.annotation.Code;

@Code(name = "이수 구분" , description = "과목의 이수 구분 타입을 나타냄")
public enum CourseType {

    @Code(name = "전공 필수", description = "전공자는 필수로 들어야 하는 과목")
    MAJOR_REQUIRED,  //전공 필수
    @Code(name = "전공 선택", description = "전공자는 선택적으로 들을수 있는 과목")
    MAJOR_ELECTIVE, //전공 선택
    @Code(name = "교양 필수", description = "필수적으로 들어야 하는 교양 과목")
    GENERAL_REQUIRED, //교양 필수
    @Code(name = "교양 선택", description = "선택적으로 들을수 있는 교양 과목")
    GENERAL_ELECTIVE, //교양 선택
    @Code(name = "자유 선택", description = "비전공자도 자유롭게 들을 수 있는 과목")
    FREE_ELECTIVE //자유 선택
}
