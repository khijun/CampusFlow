package edu.du.campusflow.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MilitaryInfo {

    @Id
    private Long id;

    @OneToOne
    @JoinColumn(name = "student_id")
    @MapsId
    private Student student;                     //학번

    @ManyToOne
    @JoinColumn(name = "discharge_type", referencedColumnName = "code_id")
    private CommonCode dischargeType;           // 전역 구분 코드

    private String finalRank;                   // 최종 군 계급

    private String serviceNumber;               //군번

}
