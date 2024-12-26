package edu.du.campusflow.enums;

import edu.du.campusflow.annotation.Code;

@Code(name = "건물명", description = "건물의 명칭을 나타냄")
public enum Building {

    @Code(name = "1호관", description = "건물이 1호관임을 나타냄")
    ONE,
    @Code(name = "2호관", description = "건물이 2호관임을 나타냄")
    TWO,
    @Code(name = "3호관", description = "건물이 3호관임을 나타냄")
    THREE,
    @Code(name = "4호관", description = "건물이 4호관임을 나타냄")
    FOUR,
    @Code(name = "5호관", description = "건물이 5호관임을 나타냄")
    FIVE
}
