package edu.du.campusflow.controller;

import edu.du.campusflow.entity.Notice;
import edu.du.campusflow.service.NoticeService; // NoticeService 추가
import edu.du.campusflow.entity.Staff; // Staff 엔티티 추가
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal; // 인증된 사용자 가져오기
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

@Controller
public class NoticeController {

    @Autowired
    private NoticeService noticeService; // NoticeService 주입

    // 공지 추가 페이지
    @GetMapping("/notice/add")
    public String showAddNoticeForm() {
        return "notice/addNotice"; // 공지 추가 페이지로 이동
    }

    // 공지 추가 처리
    @PostMapping("/notice")
    public String addNotice(Notice notice, @AuthenticationPrincipal Staff staff) {
        notice.setStaff(staff); // 현재 로그인한 교직원 정보 설정
        notice.setCreatedAt(LocalDateTime.now()); // 생성 날짜 설정
        noticeService.createNotice(notice); // 공지 생성
        return "redirect:/notice/view"; // 공지 추가 후 목록 페이지로 리다이렉트
    }

    // 모든 공지 조회 페이지
    @GetMapping("/notice/view")
    public String viewNotices(Model model) {
        model.addAttribute("notices", noticeService.getAllNotices()); // 모든 공지를 모델에 추가
        return "notice/viewNotices"; // 공지 목록 페이지로 이동
    }
}