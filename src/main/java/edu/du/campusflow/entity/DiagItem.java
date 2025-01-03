package edu.du.campusflow.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "diag_items") // 강의평가 답변
public class DiagItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment 사용
    @Column(name = "answer_id")
    private Long answerId; // answer_id, BIGINT -> Long으로 매핑

    @ManyToOne
    @JoinColumn(name = "ofregistration_id")
    private Ofregistration ofRegistration; // 외래 키: ofregistration_id

    @ManyToOne
    @JoinColumn(name = "question_id")
    private DiagQuestion diagQuestion; // 외래 키: question_id

    @Column(name = "score")
    private Integer score; // score, INT -> Integer로 매핑
}
