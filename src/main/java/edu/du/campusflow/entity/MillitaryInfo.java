package edu.du.campusflow.entity;

import javax.persistence.*;

@Entity
public class MillitaryInfo {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "student_id")
    private Student student;                     //학번

    @ManyToOne
    @JoinColumn(name = "discharge_type", referencedColumnName = "code_id")
    private CommonCode discharge_type;           // 전역 구분

    private String final_rank;                   // 최종 군 계급

    private String service_number;               //군번

}
