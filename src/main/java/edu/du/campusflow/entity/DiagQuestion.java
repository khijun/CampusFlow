package edu.du.campusflow.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "diag_questions") // 진단평가 문항
public class DiagQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id", nullable = false)
    private Long questionId; // question_id, BIGINT -> Long으로 매핑

    @Column(name = "question_name")
    private String questionName; // question_name, VARCHAR(255) -> String으로 매핑
}
