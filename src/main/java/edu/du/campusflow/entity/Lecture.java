package edu.du.campusflow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Table(name = "lecture")
public class Lecture { //강의에 관한 데이터를 저장하는 엔티티

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_id")
    private Long lectureId; //강의 아이디

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor; //교수 아이디

    @ManyToOne
    @JoinColumn(name = "course_record_id")
    private Course course; // 강의에 대한 교과목 기록 (course 테이블의 기록 아이디만 참조)

    @ManyToOne
    @JoinColumn(name = "file_id")
    private FileInfo fileInfo; //강의계획서 파일 아이디

    @Column(name = "lecture_name", length = 20)
    private String lectureName; //강의명

    @Column(name = "max_students")
    private Integer maxStudents; //강의 정원

    @Column(name = "current_students")
    private Integer currentStudents; //강의 수강 인원

    @ManyToOne
    @JoinColumn(name = "semester", referencedColumnName = "code_id")
    private CommonCode semester; // 학기 정보

    @ManyToOne
    @JoinColumn(name = "lecture_status", referencedColumnName = "code_id")
    private CommonCode lectureStatus;  //강의 상태
}
