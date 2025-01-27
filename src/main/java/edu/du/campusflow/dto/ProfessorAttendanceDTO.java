package edu.du.campusflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorAttendanceDTO {

    private Long studentId;
    private String studentName = "";  // 🔹 기본값 ""
    private String lectureName = "";  // 🔹 기본값 ""
    private Long lectureId;

    private Integer attendanceCount = 0;  // 🔹 기본값 0
    private Integer lateCount = 0;  // 🔹 기본값 0
    private Integer absentCount = 0;  // 🔹 기본값 0

    private String week1 = "-";
    private String week2 = "-";
    private String week3 = "-";
    private String week4 = "-";
    private String week5 = "-";
    private String week6 = "-";
    private String week7 = "-";
    private String week8 = "-";
    private String week9 = "-";
    private String week10 = "-";
    private String week11 = "-";
    private String week12 = "-";
    private String week13 = "-";
    private String week14 = "-";
    private String week15 = "-";

    public ProfessorAttendanceDTO(
            Long studentId, String studentName, String lectureName, Long lectureId,
            Long attendanceCount, Long lateCount, Long absentCount,
            Long week1, Long week2, Long week3, Long week4, Long week5,
            Long week6, Long week7, Long week8, Long week9, Long week10,
            Long week11, Long week12, Long week13, Long week14, Long week15) {

        this.studentId = studentId;
        this.studentName = studentName != null ? studentName : ""; // 🔹 기본값 ""
        this.lectureName = lectureName != null ? lectureName : ""; // 🔹 기본값 ""
        this.lectureId = lectureId;

        this.attendanceCount = attendanceCount != null ? attendanceCount.intValue() : 0;
        this.lateCount = lateCount != null ? lateCount.intValue() : 0;
        this.absentCount = absentCount != null ? absentCount.intValue() : 0;

        this.week1 = convertStatus(week1);
        this.week2 = convertStatus(week2);
        this.week3 = convertStatus(week3);
        this.week4 = convertStatus(week4);
        this.week5 = convertStatus(week5);
        this.week6 = convertStatus(week6);
        this.week7 = convertStatus(week7);
        this.week8 = convertStatus(week8);
        this.week9 = convertStatus(week9);
        this.week10 = convertStatus(week10);
        this.week11 = convertStatus(week11);
        this.week12 = convertStatus(week12);
        this.week13 = convertStatus(week13);
        this.week14 = convertStatus(week14);
        this.week15 = convertStatus(week15);
    }

    private String convertStatus(Long statusCode) {
        if (statusCode == null || statusCode == -1) return "-";
        switch (statusCode.intValue()) {
            case 16: return "P";
            case 17: return "L";
            case 18: return "A";
            default: return "-";
        }
    }
}
