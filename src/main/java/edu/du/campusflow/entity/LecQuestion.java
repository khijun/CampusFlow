package edu.du.campusflow.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "lec_questions") // 강의평가 문항
public class LecQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long questionId; // question_id, BIGINT -> Long으로 매핑

    @Column(name = "question_name")
    private String questionName; // question_name, VARCHAR(255) -> String으로 매핑
}
