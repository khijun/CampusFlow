package edu.du.campusflow.enums;

import edu.du.campusflow.annotation.Code;

@Code(name="수강신청 상태", description = "수강신청의 상태를 나타내는 코드")
public enum RegStatus {
    @Code(name="신청", description = "수강신청이 요청된 상태")
    REQUESTED,  // 신청
    
    @Code(name="승인", description = "수강신청이 승인된 상태")
    APPROVED,   // 승인
    
    @Code(name="거절", description = "수강신청이 거절된 상태")
    REJECTED    // 거절
} 