package edu.du.campusflow.entity;

import edu.du.campusflow.enums.InquiryStatus;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "inquiry")
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inquiry_id")
    private Long inquiryId;

    @Column(name = "inquiry_id2", nullable = false)
    private Long inquiryId2;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "subject", length = 100)
    private String subject;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "ENUM('PENDING', 'IN_PROGRESS', 'COMPLETED')")
    private InquiryStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "response_content", columnDefinition = "TEXT")
    private String responseContent; // 답변 내용

    @Column(name = "response_created_at")
    private LocalDateTime responseCreatedAt; // 답변 작성 시간


}