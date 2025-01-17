package edu.du.campusflow.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "curriculum")
//교육과정엔티티
public class Curriculum {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "curriculum_id")
   private Long curriculumId;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "dept_id")
   private Dept dept;

   @Column(name = "curriculum_name", length = 20)
   private String curriculumName;

   @Column(name = "curriculum_year")
   private Integer curriculumYear;

   @Column(name = "created_at")
   private LocalDateTime createdAt;

   @Column(name = "updated_at")
   private LocalDateTime updatedAt;

   // 사유
   @Column(name = "reason", length = 255)
   private String reason;

   @ManyToOne
   @JoinColumn(name = "grade")
   private CommonCode grade;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "curriculum_status", referencedColumnName = "code_id")
   private CommonCode curriculumStatus;

   @ManyToOne
   @JoinColumn(name = "semester", referencedColumnName = "code_id")
   private CommonCode semester;  //학기

   // 주야 구분
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "day_night", referencedColumnName = "code_id")
   private CommonCode dayNight;

   // 교양 학점
   @Column(name = "general_credits")
   private Integer generalCredits;

   // 전공 학점
   @Column(name = "major_credits")
   private Integer majorCredits;

   // 졸업 학점
   @Column(name = "graduation_credits")
   private Integer graduationCredits;

}