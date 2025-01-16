package edu.du.campusflow.dto;

import edu.du.campusflow.entity.Dept;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class DeptDTO {
    private Long deptId;
    private String deptName;
    private Integer maxStudents;
    private String deptStatus;

    public static DeptDTO fromEntity(Dept dept) {
        return DeptDTO.builder()
                .deptId(dept.getDeptId())
                .deptName(dept.getDeptName())
                .maxStudents(dept.getMaxStudents())
                .deptStatus(dept.getDeptStatus().getCodeName())
                .build();
    }

    public static List<DeptDTO> fromEntityList(List<Dept> deptList) {
        return deptList.stream().map(DeptDTO::fromEntity).collect(Collectors.toList());
    }
}
