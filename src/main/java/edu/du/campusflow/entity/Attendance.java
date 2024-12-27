package edu.du.campusflow.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "attendance")
public class Attendance {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "attendance_id")
   private Long attendanceId;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "ofregistration_id", nullable = false) // 외래 키 매핑
   private Ofregistration ofRegistration;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "lecture_time_id", nullable = false) // 강의 시간 정보 외래 키
   private LectureTime lectureTime;

   @Column(name = "attendance_date")
   private LocalDateTime attendanceDate;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "attendance_status", referencedColumnName = "code_id")
   private CommonCode attendanceStatus; // 공통 코드 (출석 상태)

   @Column(name = "remarks", length = 200)
   private String remarks;

   @Column(name = "created_at")
   private LocalDateTime createdAt;

   @Column(name = "updated_at")
   private LocalDateTime updatedAt;
}