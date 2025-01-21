package edu.du.campusflow.dto;

import lombok.Data;

@Data
public class CurriculumSubjectDetailDTO {
   private Long curriculumSubjectId; // 교육과정 교과목 ID
   private String curriculumName; // 교육과정명
   private String subjectName; // 과목명
   private String prereqSubjectName; // 선수강 과목명
   private String subjectCredits; // 이수 학점
   private String subjectTypeName; // 이수 구분 (공통코드)
   private String gradingMethod; // 평가 방법 (공통코드)
   private String grade; // 학년 (공통코드)
   private String semester; // 학기 (공통코드)
   private String dayNight; // 주야 구분 (공통코드)
   private String curriculumStatus; // 교육과정 상태 (공통코드)
}
