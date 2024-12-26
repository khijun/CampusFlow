package edu.du.campusflow.entity;

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

    @ManyToOne
    @JoinColumn(name = "related_inquiry_id")
    private Inquiry relatedInquiry;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member memberId;

    @Column(name = "subject", length = 100)
    private String subject;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "inquiry_status",referencedColumnName = "code_id")
    private CommonCode inquiryStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "response_to_id") // 답변이 참조하는 문의 ID
    private Inquiry responseTo; // 이 문의에 대한 답변




}