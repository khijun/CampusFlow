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
    Long genderId;
}
