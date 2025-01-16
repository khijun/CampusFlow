package edu.du.campusflow.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "member_id")
    private Member member;                 // 학번

    @ManyToOne
    @JoinColumn(name = "before_code",referencedColumnName = "code_id")
    private CommonCode beforeCode;        // 변경 전 코드

    @ManyToOne
    @JoinColumn(name = "after_code",referencedColumnName = "code_id")
    private CommonCode afterCode;            // 변경 후 코드

    @ManyToOne
    @JoinColumn(name = "academic_status",referencedColumnName = "code_id")
    private CommonCode academicStatus;                 // 학적 상태

    @ManyToOne
    @JoinColumn(name = "application_status",referencedColumnName = "code_id")
    private CommonCode applicationStatus;            // 신청 상태

    @Column(name = "request_date")
    private LocalDate requestDate;                  // 신청 일자

    @Column(name = "reason")
    private String reason; // 사유
}
