package edu.du.campusflow.controller;

import edu.du.campusflow.entity.AcademicCalendar;
import edu.du.campusflow.service.AcademicCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class AcademicCalendarController {

    @Autowired
    private AcademicCalendarService academicCalendarService;

    // 학사 일정 추가 페이지
    @GetMapping("/academic-calendar/add")
    public String showAddAcademicCalendarForm() {
        return "calendar/addAcademicCalendar"; // 학사 일정 추가 페이지로 이동
    }

//    // 학사 일정 추가 처리 (AJAX 요청을 위한 메서드)
//    @PostMapping("/api/academic-calendar") // AJAX 요청을 위한 엔드포인트
//    public ResponseEntity<Void> addAcademicCalendar(@RequestBody AcademicCalendar academicCalendar) {
//        academicCalendarService.createAcademicCalendar(academicCalendar); // 학사 일정 생성
//        return ResponseEntity.status(201).build(); // 성공적으로 생성된 경우 201 상태 코드 반환
//    }


    // 모든 학사 일정 조회 페이지
    @GetMapping("/academic-calendar/view")
    public String viewAcademicCalendars(Model model) {
        model.addAttribute("academicCalendars", academicCalendarService.getAllAcademicCalendars()); // 모든 학사 일정을 모델에 추가
        return "calendar/view_event"; // 학사 일정 목록 페이지로 이동
    }

    @GetMapping("/api/academic-calendar")
    public List<AcademicCalendar> getAcademicCalendars() {
        return academicCalendarService.getAllAcademicCalendars(); // 모든 학사 일정 반환
    }
}