package edu.du.campusflow.enums;

import edu.du.campusflow.annotation.Code;

@Code(name = "학기", description = "현재 학기를 나타냄 1학기,2학기")
public enum semester {

    @Code(name = "1학기", description = "현재 학기가 1학기인 상태")
    FIRST, //1학기
    @Code(name = "2학기", description = "현재 학기가 2학기인 상태")
    SECOND, //2학기
    @Code(name = "여름 학기", description = "현재 여름 학기인 상태")
    SUMMER, //여름 학기 (계절 학기)
    @Code(name = "겨울 학기", description = "현재 겨울 학기인 상태")
    WINTER //겨울 학기 (계절 학기)
}
