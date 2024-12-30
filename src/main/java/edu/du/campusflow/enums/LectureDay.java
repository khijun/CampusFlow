package edu.du.campusflow.enums;

import edu.du.campusflow.annotation.Code;

@Code(name = "강의 요일", description = "강의 요일을 나타내는")
public enum LectureDay {

    @Code(name = "월요일", description = "강의 요일이 월요일임을 나타냄")
    MONDAY,

    @Code(name = "화요일", description = "강의 요일이 화요일임을 나타냄")
    TUESDAY,

    @Code(name = "수요일", description = "강의 요일이 수요일임을 나타냄")
    WEDNESDAY,

    @Code(name = "목요일", description = "강의 요일이 목요일임을 나타냄")
    THURSDAY,

    @Code(name = "금요일", description = "강의 요일이 금요일임을 나타냄")
    FRIDAY,
}
