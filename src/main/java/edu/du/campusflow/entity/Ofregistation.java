package edu.du.campusflow.entity;

import edu.du.campusflow.enums.RegStatus;
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
    @UniqueConstraint(columnNames = {"lecture_id", "student_id", "reg_status"})
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reg_status")
    private CommonCode regStatus;

    @Column(name = "retake")
    private Boolean retake;

}
