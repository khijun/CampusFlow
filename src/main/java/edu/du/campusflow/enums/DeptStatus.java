package edu.du.campusflow.enums;

import edu.du.campusflow.annotation.Code;

@Code(name="학과 상태", description = "학과의 상태를 나타냄")
public enum DeptStatus {
    @Code(name = "활성화", description = "활성화 상태")
    ACTIVE,
    @Code(name = "비활성화", description = "비활성화 상태")
    INACTIVE,
    @Code(name = "대기중", description = "활성 대기중 상태")
    PENDING;
}
