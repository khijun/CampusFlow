package edu.du.campusflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeRequestDTO {
    private Long memberId;           // 회원 ID
    private Long newStatusCodeId; // 신청할 상태 코드 ID
    private String reason;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate leaveStartDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate leaveEndDate;
}