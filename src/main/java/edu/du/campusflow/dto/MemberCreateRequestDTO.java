package edu.du.campusflow.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class MemberCreateRequestDTO {
    List<MemberCreateDTO> memberDTOs;
    Long deptId;
    Boolean isActive;
    Long academicStatusId;
    Long memberTypeId;
    LocalDate startDate;
}
