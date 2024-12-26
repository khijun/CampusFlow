package edu.du.campusflow.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class FamilyInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;             //학번

    private String family_relation;      //가족 관계

    private String name;                 // 가족 이름

    private LocalDateTime birth_date;      // 생년월일

    private String contact;               //전화번호


}