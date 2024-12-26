package edu.du.campusflow.enums;

import edu.du.campusflow.annotation.Code;

@Code(name = "교육과정 상태", description = "교육과정의 상태를 나타냄")
public enum CurriculumStatus {
   @Code(name = "활성", description = "현재 운영 중이며, 학생들이 등록하거나 활용할 수 있는 상태")
   ACTIVE,
   @Code(name = "비활성", description = "현재 운영되지 않고 있지만, 완전히 삭제되지 않은 상태")
   INACTIVE,
   @Code(name = "사용 중단", description = "더 이상 사용되지 않으며, 사실상 폐지된 상태")
   DEPRECATED;
}
