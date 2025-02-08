package edu.du.campusflow.controller;

import edu.du.campusflow.dto.*;
import edu.du.campusflow.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('STAFF', 'PROFESSOR')")
    public String getAttendanceProfessorSearchPage() {
        return "view/iframe/attendance/attendance_professor_list";
    }

    @GetMapping("/professor-register")
    @PreAuthorize("hasAnyRole('STAFF', 'PROFESSOR')")
    public String getAttendanceProfessorRegisterPage() {
        return "view/iframe/attendance/attendance_professor_register";
    }

    @GetMapping("/staff-list")
    @PreAuthorize("hasAnyRole('STAFF')")
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
    @PreAuthorize("hasAnyRole('STAFF', 'PROFESSOR')")
    @ResponseBody
    public ResponseEntity<List<ProfessorAttendanceDTO>> getProfessorAttendance(
            @RequestParam("semester") Long semesterCodeId,
            @RequestParam("year") Integer year,
            @RequestParam("lectureId") Long lectureId) {
        return ResponseEntity.ok(attendanceService.getProfessorAttendance(year, semesterCodeId, lectureId));
    }

    @GetMapping("/api/professor/lectures")
    @PreAuthorize("hasAnyRole('STAFF', 'PROFESSOR')")
    @ResponseBody
    public ResponseEntity<List<LectureListDTO>> getProfessorLectures() {
        return ResponseEntity.ok(attendanceService.getProfessorLectures());
    }


     // 1주차 출석 데이터 자동 생성 API
    @PostMapping("/api/professor/create-week1")
    @PreAuthorize("hasAnyRole('STAFF', 'PROFESSOR')")
    @ResponseBody
    public ResponseEntity<?> createWeek1Attendance(@RequestParam Long lectureId) {
        try {
            attendanceService.createWeek1Attendance(lectureId);
            return ResponseEntity.ok("1주차 출석 데이터가 생성되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("출석 데이터 생성 중 오류 발생: " + e.getMessage());
        }
    }

    @PostMapping("/api/professor/update")
    @PreAuthorize("hasAnyRole('STAFF', 'PROFESSOR')")
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
    @PreAuthorize("hasAnyRole('STAFF')")
    @ResponseBody
    public ResponseEntity<List<DeptDTO>> getDepartments() {
        List<DeptDTO> departments = attendanceService.getDepartments();
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/api/admin/lectures")
    @PreAuthorize("hasAnyRole('STAFF')")
    @ResponseBody
    public ResponseEntity<List<LectureListDTO>> getLecturesByDepartment(
            @RequestParam("deptId") Long deptId) {
        return ResponseEntity.ok(attendanceService.getLecturesByDepartment(deptId));
    }

    @GetMapping("/api/admin/attendance")
    @PreAuthorize("hasAnyRole('STAFF')")
    @ResponseBody
    public ResponseEntity<List<ProfessorAttendanceDTO>> getAttendanceForAdmin(
            @RequestParam("semester") Long semesterCodeId,
            @RequestParam("year") Integer year,
            @RequestParam("lectureId") Long lectureId) {
        List<ProfessorAttendanceDTO> attendanceData = attendanceService.getProfessorAttendanceForAdmin(year, semesterCodeId, lectureId);
        return ResponseEntity.ok(attendanceData);
    }
}
