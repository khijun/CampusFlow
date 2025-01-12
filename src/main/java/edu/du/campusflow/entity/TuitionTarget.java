package edu.du.campusflow.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tuitionTarget")
public class TuitionTarget{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "target_id")
    private Long targetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tuition_id")
    private Tuition tuitionId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "paid_amount")
    private Integer paidAmount;

    @Column(name = "paid_date")
    private LocalDateTime paidDate;

    @Column(name = "payment_status")
    private boolean paymentStatus;
}