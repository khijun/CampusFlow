package edu.du.campusflow.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CurriculumDTO {
   private Long curriculumId;      // 교육과정 ID
   private Long deptId;            // 학과 ID
   private String deptName;        // 학과 이름
   private String curriculumName;  // 교육과정명
   private Integer curriculumYear; // 교육과정 연도
   private LocalDateTime createdAt; // 생성일
   private LocalDateTime updatedAt; // 수정일
   private String grade;           // 학년 (`code_name`으로 변경)
   private String curriculumStatus; // 교육과정 상태 (`code_name`으로 변경)
   private String semester;        // 학기 (`code_name`으로 변경)
   private String dayNight;        // 주야 구분 (`code_name`으로 변경)
   private String reason;          // 사유
}
