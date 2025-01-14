package edu.du.campusflow.dto;

import lombok.Data;

import java.util.List;

@Data
public class CurriculumDTO {
   private Long deptId;
   private String curriculumName;
   private Integer year;
   private Integer gradeCapacity;
   private String curriculumStatus; // code_value로 받음
   private String grade;           // code_value로 받음
   private String dayNight;        // code_value로 받음
   private String gradingMethod;   // code_value로 받음
   private String reason;          // 사유

   private List<Long> subjectIds;  // 과목 ID 목록
   private List<String> semesters; // 학기 목록 (code_value로 받음)
}

