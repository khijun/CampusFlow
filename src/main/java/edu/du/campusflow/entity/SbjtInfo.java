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
@Table(name = "sbjt_info")
public class SbjtInfo { //

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 생성 전략 (자동 증가)
    @Column(name = "course_id", nullable = false) // course_id 컬럼은 NOT NULL
    private Long courseId; // 과목 코드

    @Column(name = "course_name", length = 20, nullable = true) // course_name 컬럼 (최대 길이 20, NULL 허용)
    private String courseName; // 과목 명

    @Column(name = "course_desc", length = 255, nullable = true) // course_desc 컬럼 (최대 길이 255, NULL 허용)
    private String courseDesc; // 과목 설명

    @Column(name = "course_credits", nullable = true) // course_credits 컬럼 (NULL 허용)
    private Integer courseCredits; //이수 학점
}
