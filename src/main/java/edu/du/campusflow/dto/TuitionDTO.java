package edu.du.campusflow.dto;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class TuitionDTO {

    private Integer Amount;
    private Integer paidAmount;

    // 생성자, Getter/Setter 생략

    public String tuiAmountFormatted() {
        return formatAmount(Amount);
    }

    public String schAmountFormatted() {
        return formatAmount(paidAmount);
    }

    private String formatAmount(Integer amount) {
        if (amount == null) {
            return "0";  // null인 경우 처리
        }
        return String.format("%,d", amount);  // 쉼표 구분
    }
}
