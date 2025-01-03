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
public class EducationInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;            //학번

    private String schoolName;              //학교명

    private LocalDateTime enrollmentDate;         //입학 날짜

    private LocalDateTime graduationDate;           //졸업 날짜

    @ManyToOne
    @JoinColumn(name = "graduation_status", referencedColumnName = "code_id")
    private CommonCode graduationStatus;               // 졸업 여부 코드
}
