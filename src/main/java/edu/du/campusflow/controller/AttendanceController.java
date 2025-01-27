package edu.du.campusflow.controller;

import edu.du.campusflow.dto.AttendanceDTO;
import edu.du.campusflow.dto.LectureListDTO;
import edu.du.campusflow.dto.ProfessorAttendanceDTO;
import edu.du.campusflow.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping("/student-search")
    public String getAttendanceStudentSearchPage() {
        return "view/iframe/attendance/attendance_student_search";
    }

    @GetMapping("/professor-search")
    public String getAttendanceProfessorSearchPage() {
        return "view/iframe/attendance/attendance_professor_list";
    }

    @GetMapping("/professor-register")
    public String getAttendanceProfessorRegisterPage() {
        return "view/iframe/attendance/attendance_professor_register";
    }

    @GetMapping("/api/student")
    @ResponseBody
    public ResponseEntity<List<AttendanceDTO>> getStudentAttendance(
            @RequestParam("semester") Long semesterCodeId,
            @RequestParam("year") Integer year) {

        return ResponseEntity.ok(attendanceService.getStudentAttendance(semesterCodeId, year));
    }

    @GetMapping("/api/professor")
    @ResponseBody
    public ResponseEntity<List<ProfessorAttendanceDTO>> getProfessorAttendance(
            @RequestParam("semester") Long semesterCodeId,
            @RequestParam("year") Integer year,
            @RequestParam("lectureId") Long lectureId) {

        return ResponseEntity.ok(attendanceService.getProfessorAttendance(year, semesterCodeId, lectureId));
    }

    @GetMapping("/api/professor/lectures")
    @ResponseBody
    public ResponseEntity<List<LectureListDTO>> getProfessorLectures() {
        return ResponseEntity.ok(attendanceService.getProfessorLectures());
    }

    @PostMapping("/api/professor/update")
    @ResponseBody
    public ResponseEntity<Map<String, String>> updateAttendance(
            @RequestParam("lectureId") Long lectureId,
            @RequestBody List<ProfessorAttendanceDTO> attendanceList) {

        if (lectureId == null || attendanceList == null || attendanceList.isEmpty()) {
            throw new IllegalArgumentException("❌ 요청 데이터가 올바르지 않습니다.");
        }

        System.out.println("🔹 요청된 출결 데이터: " + attendanceList);

        attendanceService.updateAttendance(lectureId, attendanceList);

        return ResponseEntity.ok(Map.of("message", "출결 정보가 정상적으로 수정되었습니다."));
    }
}
