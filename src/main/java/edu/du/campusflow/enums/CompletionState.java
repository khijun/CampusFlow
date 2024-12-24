package edu.du.campusflow.enums;

import edu.du.campusflow.annotation.Code;

@Code(name = "수료 상태", description = "강의 수료 상태를 나타냄")
public enum CompletionState {
   @Code(name = "수료 완료", description = "강의를 수료한 상태")
   COMPLETED,

   @Code(name = "미수료", description = "강의를 아직 수료하지 않은 상태")
   NOT_COMPLETED,

   @Code(name = "진행 중", description = "강의를 진행 중인 상태")
   IN_PROGRESS;
}
