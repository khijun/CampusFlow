package edu.du.campusflow.enums;

import edu.du.campusflow.annotation.Code;

@Code(name = "전역 구분",description = "만기 전역 또는 의가사 전역")
public enum MillitaryStatus {
    @Code(name = "만기 전역",description = "만기 전역 상태")
    FULL_DISCHARGE,
    @Code(name = "의가사 전역",description = "의가사 전역 상태")
    HARDSHIP_DISCHARGE
}
