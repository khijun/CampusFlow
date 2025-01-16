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
@Table(name = "lecture_time")
public class LectureTime { //강의 시간 정보를 저장하는 엔티티

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_time_id")
    private Long lectureTimeId; //강의 교시

    @ManyToOne
    @JoinColumn(name = "lecture_week_id")
    private LectureWeek lectureWeek; // 강의 주차 아이디

    @ManyToOne
    @JoinColumn(name = "facility_id")
    private Facility facility; //강의실 아이디

    @ManyToOne
    @JoinColumn(name = "lecture_day")
    private CommonCode lectureDay; //강의 요일

    @ManyToOne
    @JoinColumn(name = "start_time")
    private CommonCode startTime; //시작 시간

    @ManyToOne
    @JoinColumn(name = "end_time")
    private CommonCode endTime; //종료 시간

    @ManyToOne
    @JoinColumn(name = "class_status")
    private CommonCode classStatus;  // 수업 상태

}
