package edu.du.campusflow.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ofregistration")
public class Ofregistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")  // 이 부분 추가
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lectureId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "reg_date")
    private LocalDate regDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reg_status")
    private CommonCode regStatus;

    @Column(name = "retake")
    private Boolean retake;

}
