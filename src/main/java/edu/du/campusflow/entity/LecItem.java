package edu.du.campusflow.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "lec_items") // 테이블 이름을 명시
public class LecItem {

    @Id
    @Column(name = "answer_id")
    private Long answerId; // answer_id, bigint는 Long으로 매핑

    @Column(name = "question_id", nullable = false)
    private Long questionId; // question_id, NOT NULL

    @Column(name = "answer_name")
    private String answerName; // answer_name, VARCHAR(255)

    @Column(name = "score")
    private String score; // score, VARCHAR(255)
}