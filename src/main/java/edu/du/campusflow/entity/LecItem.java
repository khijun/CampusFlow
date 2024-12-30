package edu.du.campusflow.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "lec_items") // 강의평가 답변
public class LecItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id", nullable = false)
    private Long answerId; // answer_id, BIGINT -> Long으로 매핑

    @ManyToOne
    @JoinColumn(name = "ofregistration_id", nullable = false)
    private Ofregistration ofRegistration; // 외래 키: ofregistration_id

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private LecQuestion lecQuestion; // 외래 키: question_id

    @Column(name = "score")
    private Integer score; // score, INT -> Integer로 매핑
}
