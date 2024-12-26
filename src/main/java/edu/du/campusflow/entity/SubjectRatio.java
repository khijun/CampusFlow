package edu.du.campusflow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "subject_ratio")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SubjectRatio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_ratio_id")
    private Long subjectRatioId; //비율 아이디

    @ManyToOne
    @JoinColumn(name = "course_id")
    private SubjectInfo course; //subjectInfo 엔티티 참조

    @ManyToOne
    @JoinColumn(name = "ratio_type", referencedColumnName = "code_id")
    private CommonCode ratioType; //비율 유형

    @Column(name = "ratio_value")
    private Integer ratioValue;  //비율의 비중
}
