package edu.du.campusflow.enums;

import edu.du.campusflow.annotation.Code;

@Code(name = "출석 상태", description = "학생의 출석 상태를 나타냄")
public enum AttendanceStatus {
   @Code(name = "출석", description = "학생이 정상적으로 출석한 상태")
   PRESENT,

   @Code(name = "지각", description = "학생이 지각한 상태")
   LATE,

   @Code(name = "결석", description = "학생이 결석한 상태")
   ABSENT,

   @Code(name = "공결", description = "학생이 승인된 결석 상태")
   EXCUSED;
}
