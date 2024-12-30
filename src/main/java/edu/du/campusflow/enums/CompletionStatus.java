package edu.du.campusflow.enums;

import edu.du.campusflow.annotation.Code;

@Code(name = "이수 상태", description = "강의 이수 상태를 나타냄")
public enum CompletionStatus {
   @Code(name = "이수 완료", description = "강의를 이수 완료한 상태")
   COMPLETED,

   @Code(name = "미이수", description = "강의를 아직 이수하지 않은 상태")
   NOT_COMPLETED,

   @Code(name = "진행 중", description = "강의를 진행 중인 상태")
   IN_PROGRESS;
}
