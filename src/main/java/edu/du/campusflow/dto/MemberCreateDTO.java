package edu.du.campusflow.dto;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class MemberCreateDTO {
    String name;
    String tel;
    String address;
    LocalDate birthday;
    String email;
    String gender;// 남자, 여자

    public Long getGenderId() {
        switch (this.gender) {
            case "남자":
                return 56L;
            case "여자":
                return 57L;
            default:
                return null;
        }
    }
}
