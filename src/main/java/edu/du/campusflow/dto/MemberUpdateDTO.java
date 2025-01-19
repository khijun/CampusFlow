package edu.du.campusflow.dto;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class MemberUpdateDTO {
    String name;
    String tel;
    String address;
    LocalDate birthDate;
    String email;
    String currentPassword;
    String newPassword;
}
