package edu.du.campusflow.enums;

import edu.du.campusflow.annotation.Code;

@Code(name = "강의 상태", description = "현재 강의 상태를 나타냄")
public enum LectureStatus {

    @Code(name = "대기", description = "현재 강의 신청을 받는 상태")
    PENDING, //대기
    @Code(name = "개강", description = "현재 강의가 개강중인 상태")
    STARTED, //개강
    @Code(name = "마감", description = "현재 강의 신청이 마감된 상태")
    CLOSED, //마감
    @Code(name = "폐강", description = "강의가 폐강되어 사라진 상태")
    CANCELLED //폐강
}
