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
@Table(name = "subject")
public class Subject { //

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 생성 전략 (자동 증가)
    @Column(name = "subject_id") // subject_id 컬럼은 NOT NULL
    private Long subjectId; // 과목 코드

    @Column(name = "subject_name", length = 20) // subject_name 컬럼 (최대 길이 20, NULL 허용)
    private String subjectName; // 과목 명

    @Column(name = "subject_desc", length = 255) // subject_desc 컬럼 (최대 길이 255, NULL 허용)
    private String subjectDesc; // 과목 설명

    @Column(name = "subject_credits") // subject_credits 컬럼 (NULL 허용)
    private Integer subjectCredits; //이수 학점
}
