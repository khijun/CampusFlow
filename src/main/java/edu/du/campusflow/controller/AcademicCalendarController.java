package edu.du.campusflow.controller;

import edu.du.campusflow.entity.AcademicCalendar;
import edu.du.campusflow.service.AcademicCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AcademicCalendarController {

    @Autowired
    private AcademicCalendarService academicCalendarService;

    // 학사 일정 추가 페이지
    @GetMapping("/academic-calendar/add")
    public String showAddAcademicCalendarForm() {
        return "calendar/addAcademicCalendar"; // 학사 일정 추가 페이지로 이동
    }

    // 학사 일정 추가 처리
    @PostMapping("/academic-calendar")
    public String addAcademicCalendar(AcademicCalendar academicCalendar) {
        academicCalendarService.createAcademicCalendar(academicCalendar); // 학사 일정 생성
        return "redirect:/academic-calendar/view"; // 학사 일정 추가 후 목록 페이지로 리다이렉트
    }


    // 모든 학사 일정 조회 페이지
    @GetMapping("/academic-calendar/view")
    public String viewAcademicCalendars(Model model) {
        model.addAttribute("academicCalendars", academicCalendarService.getAllAcademicCalendars()); // 모든 학사 일정을 모델에 추가
        return "calendar/view_event"; // 학사 일정 목록 페이지로 이동
    }
}