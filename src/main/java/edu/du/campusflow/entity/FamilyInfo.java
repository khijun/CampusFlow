package edu.du.campusflow.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FamilyInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;             //학번

    @ManyToOne
    @JoinColumn(name = "family_relation",referencedColumnName = "code_id")
    private CommonCode familyRelation;      //가족 관계

    private String name;                 // 가족 이름

    private LocalDateTime birthDate;      // 생년월일

    private String contact;               //전화번호


}
