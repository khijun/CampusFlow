package edu.du.campusflow.enums;

import edu.du.campusflow.annotation.Code;

@Code(name = "학적 상태", description = "학생의 현재 학적 상태를 나타냄")
public enum AcademicStatus {
    @Code(name = "재학", description = "재학중")
    ENROLLED,           // 재학

    @Code(name = "휴학", description = "휴학중")
    LEAVE_OF_ABSENCE,   // 휴학

    @Code(name = "졸업", description = "졸업을 완료한 상태")
    GRADUATED,          // 졸업

    @Code(name = "제적", description = "제적당한 상태")
    EXPELLED,           // 제적

    @Code(name = "퇴학", description = "퇴학당한 상태")
    WITHDRAWN,          // 퇴학

    @Code(name = "수료", description = "학위 요건을 충족하지 못한 수료 상태")
    COMPLETED,          // 수료 (학위 미취득, 과정을 마친 상태)

    @Code(name = "복학 대기", description = "휴학 후 복학을 대기하는 상태")
    RETURN_FROM_LEAVE,  // 휴학 복학 대기

    @Code(name = "복학", description = "휴학 후 복학한 상태")
    RETURNED,           // 복학

    @Code(name = "졸업 유예", description = "졸업을 유예한 상태")
    DEFERRED,           // 졸업 유예

    @Code(name = "휴학 예정", description = "향후 휴학 예정인 상태")
    FUTURE_LEAVE,        // 휴학 예정

    @Code(name = "졸업 예정", description = "졸업 예정인 상태")
    FUTURE_GRADUATED       // 졸업 예정
}

