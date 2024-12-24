package edu.du.campusflow.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "diag_items") // 테이블 이름을 명시
public class DiagItem {

    @Id
    @Column(name = "answer_id", nullable = false)
    private Long answerId; // answer_id, BIGINT -> Long으로 매핑

    @Column(name = "ofregistration_id", nullable = false)
    private Long ofregistrationId; // ofregistration_id, BIGINT -> Long으로 매핑

    @Column(name = "question_id", nullable = false)
    private Long questionId; // question_id, BIGINT -> Long으로 매핑

    @Column(name = "score")
    private Integer score; // score, INT -> Integer로 매핑
}
