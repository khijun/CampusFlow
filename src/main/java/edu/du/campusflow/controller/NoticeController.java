package edu.du.campusflow.controller;

import edu.du.campusflow.entity.Member;
import edu.du.campusflow.entity.Notice;
import edu.du.campusflow.service.NoticeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/iframe/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    private void checkStaffAccess() {
        if (!noticeService.isStaff()) {
            throw new AccessDeniedException("교직원만 접근할 수 있습니다.");
        }
    }

    // 공지 추가 페이지
    @GetMapping("/add")
    public String showAddNoticeForm() {
        checkStaffAccess();
        return "/view/iframe/notice/addNotice";
    }

    // 공지 추가 처리
    @PostMapping
    public String addNotice(Notice notice, @AuthenticationPrincipal Member member) {
        checkStaffAccess();
        notice.setMember(member);
        notice.setCreatedAt(LocalDateTime.now());
        noticeService.createNotice(notice);
        return "redirect:/iframe/notice/view";
    }

    // 공지사항 수정 폼 표시
    @GetMapping("/edit/{id}")
    public String showEditNoticeForm(@PathVariable Long id, Model model) {
        checkStaffAccess();
        Notice notice = noticeService.getNoticeById(id);
        model.addAttribute("notice", notice);
        return "/view/iframe/notice/editNotice";
    }

    // 공지사항 수정 처리
    @PostMapping("/edit/{id}")
    public String editNotice(@PathVariable Long id, Notice notice) {
        checkStaffAccess();
        noticeService.updateNotice(id, notice);
        return "redirect:/iframe/notice/{id}";
    }

    // 공지사항 삭제 처리
    @PostMapping("/delete/{id}")
    public String deleteNotice(@PathVariable Long id) {
        checkStaffAccess();
        noticeService.deleteNotice(id);
        return "redirect:/iframe/notice/view";
    }

    // 모든 공지 조회 페이지 (모든 사용자 접근 가능)
    @GetMapping("/view")
    public String viewNotices(@RequestParam(value = "searchType", required = false) String searchType,
                              @RequestParam(value = "keyword", required = false) String keyword, 
                              @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "8") int size,
                              Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Notice> noticePage;
        if (keyword != null && !keyword.isEmpty()) {
            if ("member".equals(searchType)) {
                noticePage = noticeService.searchNoticesByMember(keyword, pageable);
            } else {
                noticePage = noticeService.searchNoticesBySubject(keyword, pageable);
            }
        } else {
            noticePage = noticeService.getAllNotices(pageable);
        }
        model.addAttribute("noticePage", noticePage);
        model.addAttribute("isStaff", noticeService.isStaff());
        return "/view/iframe/notice/viewNotice";
    }

    // 특정 공지사항 상세 조회
    @GetMapping("/{id}")
    public String viewNoticeDetail(@PathVariable Long id, Model model) {
        Notice notice = noticeService.getNoticeById(id);

        // 게시물 번호 계산
        long noticeNumber = noticeService.calculateNoticeNumber(notice.getNoticeId());

        model.addAttribute("notice", notice);
        model.addAttribute("noticeNumber", noticeNumber);
        model.addAttribute("isStaff", noticeService.isStaff());
        return "/view/iframe/notice/noticeDetail";
    }
}