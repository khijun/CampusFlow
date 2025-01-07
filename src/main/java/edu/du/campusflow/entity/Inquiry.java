package edu.du.campusflow.entity;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Column(name = "subject", length = 100)
    private String subject;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_inquiry")
    private Inquiry responseTo;

    @ManyToOne
    @JoinColumn(name = "inquiry_status", referencedColumnName = "code_id")
    private CommonCode inquiryStatus;

    @OneToMany(mappedBy = "responseTo", fetch = FetchType.LAZY)
    private List<Inquiry> comments = new ArrayList<>();

    
}