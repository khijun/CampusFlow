package edu.du.campusflow.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "completion_id")
    private Completion completion;              // 이수 테이블 생성 시 주석 풀기

    @ManyToOne
    @JoinColumn(name = "grade_type",referencedColumnName = "code_id")
    private CommonCode gradeType;             // 점수 유형

    private int score;              //점수
}
