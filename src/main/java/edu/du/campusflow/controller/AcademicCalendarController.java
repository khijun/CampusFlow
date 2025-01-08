package edu.du.campusflow.controller;

import edu.du.campusflow.entity.AcademicCalendar;
import edu.du.campusflow.service.AcademicCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AcademicCalendarController {

    @Autowired
    private AcademicCalendarService academicCalendarService;

    @GetMapping("/iframe/calendar/addAcademicCalendar") // 추가 페이지를 보여주는 메서드
    public String showAddCalendarPage(Model model) {
        return "calendar/addAcademicCalendar";
    }
    @GetMapping ("/iframe/calendar/view_event")// 학사 일정 뷰 화면을 보여주는 메서드
    public String showCalendarPage(Model model) {
        List<AcademicCalendar> calendars = academicCalendarService.getAllCalendars();
        model.addAttribute("calendars", calendars); // 모델에 일정 추가
        return "/calendar/view_event";
    }

    @PostMapping
    public ResponseEntity<Void> addAcademicCalendar(@RequestBody AcademicCalendar academicCalendar) {
        return academicCalendarService.addAcademicCalendar(academicCalendar);
    }

    @GetMapping("/iframe/calendar/api/academic-calendar") // 일정 목록을 가져오는 API 엔드포인트
    @ResponseBody // JSON 형식으로 응답
    public ResponseEntity<List<AcademicCalendar>> getAcademicCalendars() {
        List<AcademicCalendar> calendars = academicCalendarService.getAllCalendars(); // 모든 학사 일정 가져오기
        return ResponseEntity.ok(calendars); // 200 OK 응답과 함께 일정 목록 반환
    }
}