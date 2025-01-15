package edu.du.campusflow.dto;

import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.Dept;
import edu.du.campusflow.entity.FileInfo;
import edu.du.campusflow.entity.Member;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class MemberCreateDTO {
    Long deptId;
    String name;
    String tel;
    String address;
    LocalDate birthday;
    String email;
    Long genderId;
    Long academicStatusId;

    public static Member toEntityFromCreateDTO(Long memberId, Dept dept,
                                               String pw, String name, String tel, String address, LocalDate birthDate, Boolean isActive, LocalDateTime createAt, LocalDateTime updateAt, String email,
                                               FileInfo fileInfo, CommonCode gender, CommonCode academicStatus, CommonCode grade,
                                               CommonCode memberType, LocalDate startDate, LocalDate endDate) {
        return Member.builder()
                .memberId(memberId)
                .dept(dept)
                .password(pw)
                .name(name)
                .tel(tel)
                .address(address)
                .birthDate(birthDate)
                .isActive(isActive)
                .createAt(createAt)
                .updateAt(updateAt)
                .email(email)
                .fileInfo(fileInfo)
                .gender(gender)
                .academicStatus(academicStatus)
                .grade(grade)
                .memberType(memberType)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}
