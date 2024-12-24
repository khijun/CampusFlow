package edu.du.campusflow.entity;

import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.Student;
import edu.du.campusflow.entity.Tuition;
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
    @JoinColumn(name = "tuition_id", nullable = false)
    private Tuition tuitionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tui_year", nullable = false)
    private Tuition tuiYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester")
    private CommonCode semester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "paid_amount")
    private Long paidAmount;

    @Column(name = "paid_status")
    private Boolean paidStatus;

    @Column(name = "paid_date")
    private LocalDateTime paidDate;
}