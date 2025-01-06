package edu.du.campusflow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "lecture_week")
public class    LectureWeek { //강의 주차 데이터를 저장하는 엔티티

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_week_id")
    private Long lectureWeekId; //강의 주차 아이디

    @ManyToOne
    @JoinColumn(name = "lecture_id")
    private Lecture lecture; // 강의 아이디

    @Column(name = "lecture_week_name", length = 10)
    private String lectureWeekName; // 강의 주차 명

    @Column(name = "week")
    private Integer week; // 1주차, 2주차 할때 주차정보
}
