package edu.du.campusflow.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ChangeHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;          //학번


    @ManyToOne
    @JoinColumn(name = "before_code",referencedColumnName = "code_id")
    private CommonCode beforeCode;            // 변경 전 코드

    @ManyToOne
    @JoinColumn(name = "after_code",referencedColumnName = "code_id")
    private CommonCode afterCode;            // 변경 후 코드

    private LocalDateTime approvalDate;        //승인 일자

    private int grade;         //학년
}
