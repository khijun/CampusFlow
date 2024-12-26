package edu.du.campusflow.enums;

import edu.du.campusflow.annotation.Code;

@Code(name = "층수", description = "건물의 층수를 나타냄")
public enum Floor {

    @Code(name = "1층", description = "건물의 1층임을 나타냄")
    FIRST,

    @Code(name = "2층", description = "건물의 2층임을 나타냄")
    SECOND,

    @Code(name = "3층", description = "건물의 3층임을 나타냄")
    THIRD,

    @Code(name = "4층", description = "건물의 4층임을 나타냄")
    FOURTH,

    @Code(name = "5층", description = "건물의 5층임을 나타냄")
    FIFTH
}
