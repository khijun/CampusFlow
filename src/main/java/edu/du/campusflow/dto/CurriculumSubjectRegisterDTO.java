package edu.du.campusflow.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CurriculumSubjectRegisterDTO {
   @NotNull(message = "교육과정 ID는 필수입니다.")
   private Long curriculumId; // 교육과정 ID (필수)
   private Long subjectId; // 과목 ID (필수)
   private String subjectTypeName; // 이수 구분 (공통코드)
   private String gradingMethod; // 평가 방법 (공통코드)
}
