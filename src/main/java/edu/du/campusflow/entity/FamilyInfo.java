package edu.du.campusflow.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FamilyInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "familyinfo_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;             //학번

    @ManyToOne
    @JoinColumn(name = "family_relation",referencedColumnName = "code_id")
    private CommonCode familyRelation;      //가족 관계

    private String name;                 // 가족 이름

    @Column(name = "birth_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;      // 생년월일

    private String contact;               //전화번호


}
