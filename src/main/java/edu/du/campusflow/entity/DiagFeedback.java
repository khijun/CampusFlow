package edu.du.campusflow.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "diag_feedback") // 테이블 이름을 명시
public class DiagFeedback {

    @Id
    @Column(name = "feedback_id", nullable = false)
    private Long feedbackId; // feedback_id, BIGINT -> Long으로 매핑

    @Column(name = "ofregistration_id", nullable = false)
    private Long ofregistrationId; // ofregistration_id, BIGINT -> Long으로 매핑

    @Column(name = "fd_content")
    private String fdContent; // fd_content, VARCHAR(200) -> String으로 매핑
}
