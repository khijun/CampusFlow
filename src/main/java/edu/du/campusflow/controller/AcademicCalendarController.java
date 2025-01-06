package edu.du.campusflow.controller;

import edu.du.campusflow.entity.AcademicCalendar;
import edu.du.campusflow.service.AcademicCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/calendar")
public class AcademicCalendarController {

    @Autowired
    private AcademicCalendarService academicCalendarService;

    @GetMapping("/add") // 추가 페이지를 보여주는 메서드
    public String showAddCalendarPage(Model model) {
        return "calendar/addAcademicCalendar"; // templates/calendar/addAcademicCalendar.html을 렌더링
    }
    @GetMapping // 학사 일정 뷰 화면을 보여주는 메서드
    public String showCalendarPage(Model model) {
        List<AcademicCalendar> calendars = academicCalendarService.getAllCalendars();
        model.addAttribute("calendars", calendars); // 모델에 일정 추가
        return "/calendar/view_event"; // templates/calendar.html을 렌더링
    }

    @PostMapping
    public ResponseEntity<Void> addAcademicCalendar(@RequestBody AcademicCalendar academicCalendar) {
        return academicCalendarService.addAcademicCalendar(academicCalendar);
    }

    @GetMapping("/api/academic-calendar") // 일정 목록을 가져오는 API 엔드포인트
    @ResponseBody // JSON 형식으로 응답
    public ResponseEntity<List<AcademicCalendar>> getAcademicCalendars() {
        try {
            List<AcademicCalendar> calendars = academicCalendarService.getAllCalendars();
            System.out.println("Fetched calendars: " + calendars);
            if (calendars == null) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(calendars);
        } catch (Exception e) {
            e.printStackTrace(); // 스택 트레이스 출력
            System.err.println("Error fetching calendars: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 일정 추가/수정 API
    @PostMapping("/api/academic-calendar")
    @ResponseBody
    public ResponseEntity<?> addOrUpdateCalendar(@RequestBody AcademicCalendar calendar) {
        try {
            AcademicCalendar savedCalendar = academicCalendarService.createAcademicCalendar(calendar);
            return ResponseEntity.ok(savedCalendar);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("일정 저장 실패");
        }
    }

    // 일정 수정 API
    @PutMapping("/api/academic-calendar/{id}")
    @ResponseBody
    public ResponseEntity<?> updateCalendar(@PathVariable Long id, @RequestBody AcademicCalendar calendar) {
        try {
            calendar.setCalendarId(id);
            AcademicCalendar updatedCalendar = academicCalendarService.createAcademicCalendar(calendar);
            return ResponseEntity.ok(updatedCalendar);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("일정 수정 실패");
        }
    }

    // 일정 삭제 API
    @DeleteMapping("/api/academic-calendar/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteCalendar(@PathVariable Long id) {
        try {
            academicCalendarService.deleteCalendar(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("일정 삭제 실패");
        }
    }

    @GetMapping("/list")
    public String showCalendarList() {
        return "calendar/academicCalendar";
    }
}