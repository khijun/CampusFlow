package edu.du.campusflow.entity;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notice")
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long noticeId;

    @ManyToOne(fetch = FetchType.LAZY) // Staff와의 Many-to-One 관계
    @JoinColumn(name = "staff_id") // 외래 키 설정
    private Staff staff; // 공지 작성한 교직원 엔티티

    @Column(name = "subject", length = 100)
    private String subject; // 제목

    @Column(name = "content", columnDefinition = "TEXT")
    private String content; // 내용

    @Column(name = "created_at")
    private LocalDateTime createdAt; // 생성 날짜

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 업데이트 날짜


}