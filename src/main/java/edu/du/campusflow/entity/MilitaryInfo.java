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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "military_info_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;                    //학번

    @ManyToOne
    @JoinColumn(name = "discharge_type", referencedColumnName = "code_id")
    private CommonCode dischargeType;           // 전역 구분 코드

    @Column(name = "final_rank")
    private String finalRank;                   // 최종 군 계급

    @Column(name = "service_number")
    private String serviceNumber;               //군번

}
