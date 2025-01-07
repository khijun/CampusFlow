package edu.du.campusflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeRequestDto {
    private Long memberId;           // 회원 ID
    private Long newStatusCodeId;    // 신청할 상태 코드 ID
}