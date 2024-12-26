package edu.du.campusflow.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class EducationInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    private String school_name;

    private LocalDateTime enrollment_date;

    private LocalDateTime graduation_date;

    @ManyToOne
    @JoinColumn(name = "graduation_status", referencedColumnName = "code_id")
    private CommonCode status;
}
