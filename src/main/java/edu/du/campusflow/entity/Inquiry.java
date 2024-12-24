package edu.du.campusflow.entity;

import edu.du.campusflow.enums.InquiryStatus;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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
    @JoinColumn(name = "inquiry_id2", nullable = false)
    private Inquiry inquiryId2;

    @ManyToOne
    @Column(name = "member_id", nullable = false)
    private Member memberId;

    @Column(name = "subject", length = 100)
    private String subject;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "inquiry_status")
    private CommonCode inquiryStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY) // 자기 참조 관계
    @JoinColumn(name = "response_to_id") // 답변이 참조하는 문의 ID
    private Inquiry responseTo; // 이 문의에 대한 답변

    @OneToMany(mappedBy = "responseTo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Inquiry> responses; // 이 문의에 대한 답변 목록


}