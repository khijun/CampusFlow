package edu.du.campusflow.enums;

import edu.du.campusflow.annotation.Code;

@Code(name = "신청 상태",description = "신청 상태를 확인")
public enum ApplicationStatus {
    @Code(name = "승인",description = "승인된 상태")
    ACCEPT,
    @Code(name = "거절",description = "거절된 상태")
    DENY
}
