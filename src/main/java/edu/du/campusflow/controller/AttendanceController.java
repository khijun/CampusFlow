package edu.du.campusflow.controller;

import edu.du.campusflow.dto.*;
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

    @GetMapping("/professor-search")
    public String getAttendanceProfessorSearchPage() {
        return "view/iframe/attendance/attendance_professor_list";
    }

    @GetMapping("/professor-register")
    public String getAttendanceProfessorRegisterPage() {
        return "view/iframe/attendance/attendance_professor_register";
    }

    @GetMapping("/staff-list")
    public String getAttendanceStaffListPage() {
        return "view/iframe/attendance/attendance_staff_list";
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
    public ResponseEntity<Void> updateAttendance(
            @RequestBody List<ProfessorAttendanceUpdateDTO> updateDTOList) {

        if (updateDTOList == null || updateDTOList.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        attendanceService.updateAttendance(updateDTOList);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/admin/departments")
    @ResponseBody
    public ResponseEntity<List<DeptDTO>> getDepartments() {
        List<DeptDTO> departments = attendanceService.getDepartments();
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/api/admin/lectures")
    @ResponseBody
    public ResponseEntity<List<LectureListDTO>> getLecturesByDepartment(
            @RequestParam("deptId") Long deptId) {
        return ResponseEntity.ok(attendanceService.getLecturesByDepartment(deptId));
    }

    @GetMapping("/api/admin/attendance")
    @ResponseBody
    public ResponseEntity<List<ProfessorAttendanceDTO>> getAttendanceForAdmin(
            @RequestParam("semester") Long semesterCodeId,
            @RequestParam("year") Integer year,
            @RequestParam("lectureId") Long lectureId) {
        List<ProfessorAttendanceDTO> attendanceData = attendanceService.getProfessorAttendanceForAdmin(year, semesterCodeId, lectureId);
        return ResponseEntity.ok(attendanceData);
    }
}
