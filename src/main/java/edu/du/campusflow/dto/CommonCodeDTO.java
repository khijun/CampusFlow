package edu.du.campusflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommonCodeDTO {
    private String codeValue;  // 저장될 값
    private String codeName;   // 화면에 표시될 값
}
