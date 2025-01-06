package edu.du.campusflow.dto;

import lombok.Data;
import lombok.Builder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@Builder
public class TuitionDTO {
    // 등록금 대상자 ID
    private Long targetId;
    
    // 학생 회원 ID (학번)
    private Long memberId;
    
    // 학생 이름
    private String memberName;
    
    // 소속 학과명
    private String department;
    
    // 납부해야 할 등록금 총액
    private Integer amount;
    
    // 실제 납부한 금액
    private Integer paidAmount;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paidDate;
    
    // 납부 완료 여부
    private boolean paymentStatus;

    /**
     * 등록금 총액을 포맷팅하여 반환
     * @return 천 단위 구분자가 포함된 금액 문자열
     */
    public String getFormattedAmount() {
        return String.format("%,d", amount);
    }

    /**
     * 납부 금액을 포맷팅하여 반환
     * @return 천 단위 구분자가 포함된 금액 문자열
     */
    public String getFormattedPaidAmount() {
        return String.format("%,d", paidAmount);
    }

    /**
     * 납부 일시를 포맷팅하여 반환
     * @return yyyy-MM-dd HH:mm:ss 형식의 날짜 문자열, 미납의 경우 "-" 반환
     */
    public String getFormattedPaidDate() {
        if (paidDate == null) return "-";
        return paidDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 금액을 천 단위 구분자가 포함된 형식으로 변환
     * @param amount 변환할 금액
     * @return 천 단위 구분자가 포함된 금액 문자열, null인 경우 "0" 반환
     */
    private String formatAmount(Integer amount) {
        if (amount == null) return "0";
        return String.format("%,d", amount);
    }
}
