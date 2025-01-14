package edu.du.campusflow.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberSearchFilter {
    private Long memberType;
    private Boolean isActive;
    private Long deptId;
    private String name;
    private String tel;
    private LocalDate birthStart;
    private LocalDate birthEnd;
    private LocalDateTime createAtStart;
    private LocalDateTime createAtEnd;
    private Long academicStatus;
    private Long grade;
    private LocalDate startDateStart;
    private LocalDate startDateEnd;
    private LocalDate endDateStart;
    private LocalDate endDateEnd;
}
