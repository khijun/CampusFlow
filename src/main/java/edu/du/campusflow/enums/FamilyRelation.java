package edu.du.campusflow.enums;

import edu.du.campusflow.annotation.Code;

@Code(name = "가족 관계",description = "가족 관계 여부")
public enum FamilyRelation {
    @Code(name = "부",description = "아빠")
    FATHER,
    @Code(name = "모",description = "엄마")
    MOTHER
}
