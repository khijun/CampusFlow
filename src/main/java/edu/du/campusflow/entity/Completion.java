package edu.du.campusflow.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "completion")
public class Completion {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "completion_id")
   private Long completionId;

//   @ManyToOne(fetch = FetchType.LAZY)
//   @JoinColumn(name = "ofregistration_id", referencedColumnName = "ofregistration_id")
//   private OfRegistration ofRegistration; // 수강 신청 정보와 연관

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "completion_state", referencedColumnName = "code_id")
   private CommonCode completionState; // 공통 코드와 연관 (이수 상태)

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "finalGradeStatus", referencedColumnName = "code_id")
   private CommonCode finalGradeStatus; // 공통 코드와 연관 (최종 등급)

   @Column(name = "created_at")
   private LocalDateTime createdAt;

   @Column(name = "updated_at")
   private LocalDateTime updatedAt;
}