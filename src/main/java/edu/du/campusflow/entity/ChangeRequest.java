package edu.du.campusflow.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;                   // 학번

    @ManyToOne
    @JoinColumn(name = "before_code",referencedColumnName = "code_id")
    private CommonCode before_code;        // 변경 전 코드

    @ManyToOne
    @JoinColumn(name = "after_code",referencedColumnName = "code_id")
    private CommonCode after_code;            // 변경 후 코드

    @ManyToOne
    @JoinColumn(name = "academic_status",referencedColumnName = "code_id")
    private CommonCode academic_status;                 // 학적 상태

    @ManyToOne
    @JoinColumn(name = "application_status",referencedColumnName = "code_id")
    private CommonCode application_status;            // 신청 상태

    private LocalDateTime request_date;                  // 신청 일자
}
