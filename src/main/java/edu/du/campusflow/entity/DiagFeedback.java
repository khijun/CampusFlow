package edu.du.campusflow.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "diag_feedback") // 테이블 이름을 명시
public class DiagFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment 사용
    @Column(name = "feedback_id", nullable = false)
    private Long feedbackId; // feedback_id, BIGINT -> Long으로 매핑

//    @ManyToOne
//    @JoinColumn(name = "ofregistration_id", nullable = false)
//    private OfRegistration ofRegistration; // ofregistration_id 외래키

    @Column(name = "fd_content")
    private String fdContent; // fd_content, VARCHAR(200) -> String으로 매핑
}
