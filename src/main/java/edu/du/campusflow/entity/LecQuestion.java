package edu.du.campusflow.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "lec_questions") // 테이블 이름을 명시
public class LecQuestion {

    @Id
    @Column(name = "question_id", nullable = false)
    private Long questionId; // question_id, BIGINT -> Long으로 매핑

    @Column(name = "question_name")
    private String questionName; // question_name, VARCHAR(255) -> String으로 매핑
}
