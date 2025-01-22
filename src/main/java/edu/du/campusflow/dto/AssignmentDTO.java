package edu.du.campusflow.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AssignmentDTO {

    private Long assignmentId;           // 과제 아이디
    private Long lectureId;              // 강의 아이디
    private String assignmentName;       // 과제 명
    private String description;          // 과제 설명

    private String lectureName;          //강의 명
    private String professorName;        //교수 명

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    private Integer submissionCount;     //과제 제출 인원

    private Long FileInfo;               // 파일 아이디

    private Long submissionId;               //제출한 과제 아이디
    private Long studentId;                 // 학번
    private String deptName;                  // 학과명
    private String studentName;               // 학생이름
    private LocalDateTime submissionDate;     //과제 제출일
    private Integer assignmentScore;          //제출한 과제 점수
}
