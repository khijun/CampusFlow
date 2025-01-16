package edu.du.campusflow.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class DeptSearchFilter {
    String deptName;
    Integer minStudentValue;
    Integer maxStudentValue;
    Long deptStatus;
}
