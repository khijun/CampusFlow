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
@Table(name = "assignment")
public class Assignment { //과제 정보 데이터를 저장하는 엔티티

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id")
    private Long assignmentId; //과제 아이디

    @ManyToOne
    @JoinColumn(name = "lecture_id")
    private Lecture lecture; //강의 아이디


    @Column(name = "assignment_name", length = 20)
    private String assignmentName; //과제 제목

    @Column(name = "assignment_desc", length = 255)
    private String assignmentDesc; //과제 설명

    @Column(name = "due_date")
    private LocalDateTime dueDate; //과제 마감 기한

    @ManyToOne
    @JoinColumn(name = "file_id")
    private FileInfo fileInfo; //파일 아이디

}
