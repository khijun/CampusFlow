package edu.du.campusflow.enums;


import edu.du.campusflow.annotation.Code;
import lombok.Getter;

@Getter
@Code(name="성별", description = "성별을 나타내는 코드")
public enum Gender {
    @Code(name="남성", description = "남성입니다")
    MALE,
    @Code(name="여성", description = "여성입니다")
    FEMALE;

}
