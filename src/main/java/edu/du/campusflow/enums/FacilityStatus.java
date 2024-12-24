package edu.du.campusflow.enums;

import edu.du.campusflow.annotation.Code;

@Code(name = "강의실 상태", description = "현재 강의실의 상태를 나타냄")
public enum FacilityStatus {

    @Code(name = "사용가능", description = "사용가능한 상태")
    AVAILABLE, //사용가능
    @Code(name = "수리중", description = "수리중인 상태")
    UNDER_REPAIR, //수리중
    @Code(name = "폐쇄", description = "폐쇄된 상태")
    CLOSED //폐쇄
}
