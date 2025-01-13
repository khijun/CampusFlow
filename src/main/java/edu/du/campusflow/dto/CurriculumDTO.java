package edu.du.campusflow.dto;

import lombok.Data;

@Data
public class CurriculumDTO {
   private Long deptId;
   private String curriculumName;
   private Integer year;
   private Integer gradeCapacity;
   private String curriculumStatus; // code_value로 받음
   private String grade;           // code_value로 받음
   private String semester;        // code_value로 받음 (optional)
   private String subjectType;     // code_value로 받음 (optional)
   private String dayNight;        // code_value로 받음 (optional)
   private String gradingMethod;   // code_value로 받음 (optional)
   private String reason;          // 사유
   private Long subjectId;         // optional
   private Long prereqSubjectId;   // optional
}
