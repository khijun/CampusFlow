package edu.du.campusflow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "lecture_time")
public class LectureTime { //강의 시간 정보를 저장하는 엔티티

    @Id
    @Column(name = "lecture_time_id")
    private Long lecturePeriod; //강의 교시

    @ManyToOne
    @JoinColumn(name = "lecture_week_id")
    private LectureWeek lectureWeek; // 강의 주차 아이디

    @ManyToOne
    @JoinColumn(name = "lecture_id")
    private Lecture lecture; //강의 아이디

    @ManyToOne
    @JoinColumn(name = "facility_id")
    private Facility facility; //강의실 아이디

    @Column(name = "lecture_day", length = 10)
    private String lectureDay; //강의 요일

    @Column(name = "start_time")
    private LocalTime startTime; //시작 시간

    @Column(name = "end_time")
    private LocalTime endTime; //종료 시간

    @ManyToOne
    @JoinColumn(name = "class_status", referencedColumnName = "code_id")
    private CommonCode classStatus;  // 수업 상태

}
