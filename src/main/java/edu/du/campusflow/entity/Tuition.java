package edu.du.campusflow.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Year;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tuition")
public class Tuition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tuition_id")
    private Long TuitionId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id")
    private Dept deptId;


    @Column(name = "tui_year")
    private Year tuiYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester")
    private CommonCode semester;

    @Column(name = "amount")
    private Long amount;

    //납부여부
    @Column(name = "payment_status")
    private boolean paymentStatus;
} 