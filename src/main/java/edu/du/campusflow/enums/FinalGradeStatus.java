package edu.du.campusflow.enums;

import edu.du.campusflow.annotation.Code;

@Code(name = "최종 등급", description = "강의 최종 등급을 나타냄")
public enum FinalGradeStatus {
   @Code(name = "A+", description = "최상위 등급")
   A_PLUS,

   @Code(name = "A", description = "우수 등급")
   A,

   @Code(name = "B+", description = "양호 등급")
   B_PLUS,

   @Code(name = "B", description = "보통 등급")
   B,

   @Code(name = "C+", description = "기본 등급")
   C_PLUS,

   @Code(name = "C", description = "기본 이하 등급")
   C,

   @Code(name = "D", description = "미흡 등급")
   D,

   @Code(name = "F", description = "낙제 등급")
   F;
}
