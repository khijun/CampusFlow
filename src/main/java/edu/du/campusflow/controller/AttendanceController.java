package edu.du.campusflow.controller;

import edu.du.campusflow.dto.AttendanceDTO;
import edu.du.campusflow.dto.LectureListDTO;
import edu.du.campusflow.dto.ProfessorAttendanceDTO;
import edu.du.campusflow.entity.Lecture;
import edu.du.campusflow.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping("/student-search")
    public String getAttendanceStudentSearchPage() {
        return "view/iframe/attendance/attendance_student_search";
    }

    @GetMapping("/api/student")
    @ResponseBody
    public ResponseEntity<List<AttendanceDTO>> getStudentAttendance(
            @RequestParam("semester") Long semesterCodeId,
            @RequestParam("year") Integer year) {

        List<AttendanceDTO> attendanceData = attendanceService.getStudentAttendance(semesterCodeId, year);
        return ResponseEntity.ok(attendanceData);
    }

    @GetMapping("/professor-search")
    public String getAttendanceProfessorSearchPage() {
        return "view/iframe/attendance/attendance_professor_list";
    }

    @GetMapping("/api/professor")
    @ResponseBody
    public ResponseEntity<List<ProfessorAttendanceDTO>> getProfessorAttendance(
            @RequestParam("semester") Long semesterCodeId,
            @RequestParam("year") Integer year,
            @RequestParam("lectureId") Long lectureId) {

        List<ProfessorAttendanceDTO> attendanceData = attendanceService.getProfessorAttendance(year, semesterCodeId, lectureId);
        return ResponseEntity.ok(attendanceData);
    }

    @GetMapping("/api/professor/lectures")
    @ResponseBody
    public ResponseEntity<List<LectureListDTO>> getProfessorLectures() {
        List<LectureListDTO> lectures = attendanceService.getProfessorLectures();
        return ResponseEntity.ok(lectures);
    }

}
