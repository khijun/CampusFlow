package edu.du.campusflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorAttendanceUpdateDTO {
    private Long studentId;
    private Long lectureId;

    private String week1;
    private String week2;
    private String week3;
    private String week4;
    private String week5;
    private String week6;
    private String week7;
    private String week8;
    private String week9;
    private String week10;
    private String week11;
    private String week12;
    private String week13;
    private String week14;
    private String week15;
}
