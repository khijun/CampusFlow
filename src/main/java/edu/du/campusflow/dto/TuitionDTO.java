package edu.du.campusflow.dto;

import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.Dept;
import edu.du.campusflow.entity.Tuition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.NumberFormat;
import java.time.Year;
import java.util.Locale;

/**
 * 등록금 정보를 전달하기 위한 DTO 클래스
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TuitionDTO {
    private Long tuitionId;      // 등록금 ID
    private Dept dept;           // 학과 정보
    private Year tuiYear;        // 등록금 년도
    private CommonCode semester; // 학기 정보
    private Long amount;         // 등록금 금액
    private String formattedAmount; // 포맷된 금액 (예: 1,234,567)

    /**
     * Tuition 엔티티를 DTO로 변환하는 메서드
     * @param tuition 변환할 등록금 엔티티
     * @return 변환된 등록금 DTO
     */
    public static TuitionDTO fromEntity(Tuition tuition) {
        return TuitionDTO.builder()
                .tuitionId(tuition.getTuitionId())
                .dept(tuition.getDeptId())
                .tuiYear(tuition.getTuiYear())
                .semester(tuition.getSemester())
                .amount(tuition.getAmount())
                .formattedAmount(formatAmount(tuition.getAmount()))
                .build();
    }

    /**
     * 금액을 세자리마다 콤마가 있는 형식으로 변환하는 메서드
     * @param amount 변환할 금액
     * @return 포맷된 금액 문자열
     */
    private static String formatAmount(Long amount) {
        return NumberFormat.getNumberInstance(Locale.KOREA).format(amount);
    }
} 