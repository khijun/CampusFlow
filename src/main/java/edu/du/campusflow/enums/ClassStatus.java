package edu.du.campusflow.enums;

import edu.du.campusflow.annotation.Code;

@Code(name = "수업 상태", description = "현재 수업 상태를 나타냄")
public enum ClassStatus {

    @Code(name = "진행", description = "현재 수업이 진행중인 상태")
    ONGOING,
    @Code(name = "휴강", description = "예정된 강의가 취소되어 휴강인 상태")
    CANCELLED,
    @Code(name = "예정", description = "수업이 예정되어 있는 상태")
    SCHEDULED,
    @Code(name = "종료", description = "수업이 정상적으로 완료된 상태")
    COMPLETED
}
