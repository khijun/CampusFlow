package edu.du.campusflow.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

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

    private Long FileInfo;               // 파일 아이디
}
