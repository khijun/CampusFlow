package edu.du.campusflow.controller;

import edu.du.campusflow.entity.Member;
import edu.du.campusflow.entity.Notice;
import edu.du.campusflow.service.NoticeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    // 공지 추가 페이지
    @GetMapping("/notice/add")
    public String showAddNoticeForm() {
        return "notice/addNotice";
    }

    // 공지 추가 처리
    @PostMapping("/notice")
    public String addNotice(Notice notice, @AuthenticationPrincipal Member member) {
        notice.setMember(member);
        notice.setCreatedAt(LocalDateTime.now());
        noticeService.createNotice(notice);
        return "redirect:/notice/view";
    }

    // 모든 공지 조회 페이지
    @GetMapping("/notice/view")
    public String viewNotices(Model model) {
        model.addAttribute("notices", noticeService.getAllNotices());
        // 임시로 교직원 여부 설정 (나중에 실제 인증 정보로 대체)
        model.addAttribute("isStaff", true);
        return "notice/viewNotice";
    }

    // 특정 공지사항 상세 조회
    @GetMapping("/notice/{id}")
    public String viewNoticeDetail(@PathVariable Long id, Model model) {
        Notice notice = noticeService.getNoticeById(id);
        if (notice == null) {
            return "redirect:/notice/view";
        }
        
        // 임시로 교직원 여부 설정 (나중에 실제 인증 정보로 대체)
        model.addAttribute("isStaff", true);
        model.addAttribute("notice", notice);
        
        return "notice/noticeDetail";
    }

    // 공지사항 수정 페이지
    @GetMapping("/notice/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        // 교직원 권한 확인 (나중에 실제 인증 정보로 대체)
        boolean isStaff = true;
        
        if (!isStaff) {
            throw new RuntimeException("권한이 없습니다.");
        }

        Notice notice = noticeService.getNoticeById(id);
        if (notice == null) {
            return "redirect:/notice/view";
        }

        model.addAttribute("notice", notice);
        return "notice/editNotice";
    }

    // 공지사항 수정 처리
    @PostMapping("/notice/edit/{id}")
    public String updateNotice(@PathVariable Long id, Notice updatedNotice) {
        // 교직원 권한 확인 (나중에 실제 인증 정보로 대체)
        boolean isStaff = true;
        
        if (!isStaff) {
            throw new RuntimeException("권한이 없습니다.");
        }

        Notice notice = noticeService.getNoticeById(id);
        if (notice == null) {
            return "redirect:/notice/view";
        }

        // 수정된 내용 업데이트
        updatedNotice.setNoticeId(id);  // ID 설정
        updatedNotice.setMember(notice.getMember());  // 기존 작성자 유지
        updatedNotice.setCreatedAt(notice.getCreatedAt());  // 기존 작성일 유지
        updatedNotice.setUpdatedAt(LocalDateTime.now());  // 수정일 설정
        
        noticeService.updateNotice(id, updatedNotice);
        return "redirect:/notice/" + id;
    }

    // 공지사항 삭제
    @PostMapping("/notice/delete/{id}")
    public String deleteNotice(@PathVariable Long id) {
        // 교직원 권한 확인 (나중에 실제 인증 정보로 대체)
        boolean isStaff = true;
        
        if (!isStaff) {
            throw new RuntimeException("권한이 없습니다.");
        }
        
        noticeService.deleteNotice(id);
        return "redirect:/notice/view";
    }
}