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

   @Column(name = "grade_capacity")
   private Integer gradeCapacity;

   @Column(name = "created_at")
   private LocalDateTime createdAt;

   @Column(name = "updated_at")
   private LocalDateTime updatedAt;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "curriculum_status", referencedColumnName = "code_id")
   private CommonCode curriculumStatus;

   @ManyToOne
   @JoinColumn(name = "grade")
   private CommonCode grade;


}
