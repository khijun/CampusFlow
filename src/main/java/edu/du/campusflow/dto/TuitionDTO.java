package edu.du.campusflow.dto;

import lombok.Data;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
public class TuitionDTO {
    // 등록금 대상자 ID
    private Long targetId;
    
    // 학생 회원 ID (학번)
    private Long memberId;
    
    // 학생 이름
    private String memberName;
    
    // 소속 학과명
    private Long deptId;
    
    // 납부해야 할 등록금 총액
    private Integer amount;
    
    // 실제 납부한 금액
    private Integer paidAmount;
    
    // 납부 일자
    private LocalDateTime paidDate;
    
    // 납부 완료 여부
    private boolean paymentStatus;
    
    // 납부 상태 업데이트를 위한 필드
    private boolean newPaymentStatus;

    /**
     * 등록금 총액을 포맷팅하여 반환
     */
    public String getFormattedAmount() {
        return formatAmount(amount);
    }

    /**
     * 납부 금액을 포맷팅하여 반환
     */
    public String getFormattedPaidAmount() {
        return formatAmount(paidAmount);
    }

    /**
     * 금액을 천 단위 구분자가 포함된 형식으로 변환
     */
    private String formatAmount(Integer amount) {
        if (amount == null) return "0";
        return String.format("%,d", amount);
    }
}
