package edu.du.campusflow.entity;

import edu.du.campusflow.enums.CompletionState;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "completion_status")
//수료상태엔티티
public class CompletionStatus {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "status_id")
   private Long statusId;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "student_id", referencedColumnName = "member_id")
   private Student student; // Member의 ID를 상속받은 Student와 연관

//   @ManyToOne(fetch = FetchType.LAZY)
//   @JoinColumn(name = "lecture_id", referencedColumnName = "lecture_id")
//   private Lecture lecture;

   @Enumerated(EnumType.STRING)
   @Column(name = "completion_state", length = 20)
   private CompletionState completionState;

   @Column(name = "grade")
   private Integer grade;

   @Column(name = "created_at")
   private LocalDateTime createdAt;

   @Column(name = "updated_at")
   private LocalDateTime updatedAt;

//   @ManyToOne(fetch = FetchType.LAZY)
//   @JoinColumn(name = "course_id", referencedColumnName = "course_id")
//   private Course course;
}
