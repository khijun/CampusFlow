package edu.du.campusflow.dto;

import edu.du.campusflow.entity.Dept;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class DeptApiDTO {
   private Long deptId;    // 학과 ID
   private String deptName; // 학과명

   public static DeptApiDTO fromEntity(Dept dept) {
      return new DeptApiDTO(dept.getDeptId(), dept.getDeptName());
   }

   public static List<DeptApiDTO> fromEntityList(List<Dept> deptList) {
      return deptList.stream().map(DeptApiDTO::fromEntity).collect(Collectors.toList());
   }
}
