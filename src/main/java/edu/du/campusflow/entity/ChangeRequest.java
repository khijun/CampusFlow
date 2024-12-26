package edu.du.campusflow.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ChangeRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "before_code",referencedColumnName = "code_id")
    private CommonCode before_code;

    @ManyToOne
    @JoinColumn(name = "after_code",referencedColumnName = "code_id")
    private CommonCode after_code;

    @ManyToOne
    @JoinColumn(name = "school_status",referencedColumnName = "code_id")
    private CommonCode school_status;

    private LocalDateTime request_date;
}
