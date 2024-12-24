package edu.du.campusflow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_record_id")
    private Long courseRecordId; // 기본키 이름

    @ManyToOne
    @JoinColumn(name = "curriculum_id", nullable = false)
    private Curriculum curriculum;  // Curriculum 엔티티 참조

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private SbjtInfo course; //SbjtInfo 엔티티 참조

    @ManyToOne
    @JoinColumn(name = "course_id2")
    private SbjtInfo course2; //선 수강 과목

    @Column(name = "target_year")
    private Integer targetYear;

    @ManyToOne
    @JoinColumn(name = "semester", referencedColumnName = "code_id")
    private CommonCode semester;  //학기

    @ManyToOne
    @JoinColumn(name = "course_type", referencedColumnName = "code_id")
    private CommonCode courseType; //이수 구분

}
