package edu.du.campusflow.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ChangeHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;            //학번

    @ManyToOne
    @JoinColumn(name = "request_id")
    private ChangeRequest request_id;           // 변동 신청이랑 연결

    @ManyToOne
    @JoinColumn(name = "before_code",referencedColumnName = "code_id")
    private CommonCode before_code;            // 변경 전 코드

    @ManyToOne
    @JoinColumn(name = "after_code",referencedColumnName = "code_id")
    private CommonCode after_code;            // 변경 후 코드

    private LocalDateTime approval_date;        //승인 일자

    private int grade;         //학년
}
