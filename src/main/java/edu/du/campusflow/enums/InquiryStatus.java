package edu.du.campusflow.enums;

import edu.du.campusflow.annotation.Code;
import lombok.Getter;

@Getter
@Code(name="처리 상태", description ="현재 상태")
public enum InquiryStatus {
    @Code(name ="대기", description = "대기상태")
    PENDING("PENDING"),

    @Code(name = "처리중", description =  "처리중 상태")
    IN_PROGRESS("IN_PROGRESS"),

    @Code(name = "완료", description = "완료 상태")
    COMPLETED("COMPLETED");

   private final String value;

   InquiryStatus(String value) {
       this.value = value;
   }

   public String getValue() {
       return value;
   }
}
