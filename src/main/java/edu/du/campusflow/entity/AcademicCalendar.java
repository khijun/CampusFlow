package edu.du.campusflow.entity;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "academic_calendar")
public class AcademicCalendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "calendar_id")
    private Long calendarId;

    @ManyToOne(fetch = FetchType.LAZY) // Staff와의 Many-to-One 관계
    @JoinColumn(name = "staff_id") // 외래 키 설정
    private Staff staff; // 교직원 엔티티

    @Column(name = "title", length = 100)
    private String title; // 제목

    @Column(name = "start_date")
    private LocalDateTime startDate; // 시작 날짜

    @Column(name = "end_date")
    private LocalDateTime endDate; // 종료 날짜

    @Column(name = "description", columnDefinition = "TEXT")
    private String description; //학사 일정 설명
}