package edu.du.campusflow.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class DeptCreateRequest {
    List<DeptDTO> deptDTOList;
    Long deptStatus;
}
