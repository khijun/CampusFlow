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
@Table(name = "Ofregistration", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"lecture_id", "student_id"})
})
public class Ofregistation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lecture_id", nullable = false)
    private Long lectureId;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "reg_date")
    private LocalDate regDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "reg_status")
    private RegStatus regStatus;

    @Column(name = "retake")
    private Boolean retake;

    public enum RegStatus {
        REQUESTED,  // 신청
        APPROVED,   // 승인
        REJECTED    // 거절
    }
}
