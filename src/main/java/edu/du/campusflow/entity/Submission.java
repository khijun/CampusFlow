package edu.du.campusflow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Table(name = "submission")
public class Submission { //학생이 제출한 과제 데이터를 저장하는 엔티티

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "submission_id", nullable = false)
    private Long submissionId; //과제 제출 아이디

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student; //학번

    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment; //과제 아이디

    @ManyToOne
    @JoinColumn(name = "file_id", nullable = false)
    private UploadedFile uploadedFile; //파일 아이디

//    @Column(name = "submitted_file", length = 100)
//    private String submittedFile; //제출 파일명

    @Column(name = "submission_date")
    private LocalDateTime submissionDate;  //과제 제출일

    @Column(name = "assignment_score")
    private Integer assignmentScore; //과제 점수
}
