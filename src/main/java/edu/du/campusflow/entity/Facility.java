package edu.du.campusflow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Table(name = "facility")
public class Facility { //강의실 데이터를 저장하는 엔티티

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "facility_id")
    private Long facilityId; //강의실 아이디

    @Column(name = "facility_name", length = 20)
    private String facilityName; // 강의실 이름

    @ManyToOne
    @JoinColumn(name = "building", referencedColumnName = "code_id")
    private CommonCode building; //건물명

    @ManyToOne
    @JoinColumn(name = "floor", referencedColumnName = "code_id")
    private CommonCode floor; //층수

    @Column(name = "capacity")
    private Integer capacity; //수용인원

    @ManyToOne
    @JoinColumn(name = "facility_status", referencedColumnName = "code_id")
    private CommonCode facilityStatus; //강의실 상태
}
