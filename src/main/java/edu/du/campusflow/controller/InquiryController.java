package edu.du.campusflow.controller;

import edu.du.campusflow.entity.Inquiry;
import edu.du.campusflow.service.InquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDateTime;

@Controller
public class InquiryController {

    @Autowired
    private InquiryService inquiryService;

    // 문의 추가 페이지
    @GetMapping("/inquiry/add")
    public String showAddInquiryForm() {
        return "inquiry/addInquiry";
    }

    // 문의 추가 처리
    @PostMapping("/inquiry")
    public String addInquiry(Inquiry inquiry) {
        inquiryService.createInquiry(inquiry);
        return "redirect:/inquiry/view";
    }

    // 모든 문의 조회 페이지
    @GetMapping("/inquiry/view")
    public String viewInquiries(Model model) {
        model.addAttribute("inquiries", inquiryService.getAllInquiries());
        return "inquiry/viewInquiries";
    }

    // 특정 문의 상세 조회 페이지
    @GetMapping("/inquiry/{id}")
    public String viewInquiryDetail(@PathVariable Long id, Model model) {
        // 임시로 권한 설정 (나중에 실제 인증 정보로 대체)
        boolean isStaff = true;  // 테스트용
        boolean isAuthor = true;  // 테스트용
        
        Inquiry inquiry = inquiryService.getInquiryById(id);
        model.addAttribute("inquiry", inquiry);
        model.addAttribute("isStaff", isStaff);
        model.addAttribute("isAuthor", isAuthor);
        
        return "inquiry/inquiryDetail";
    }

    // 답변 등록 처리
    @PostMapping("/inquiry/{id}/reply")
    public String addReply(@PathVariable Long id, @RequestParam String content) {
        // 임시로 교직원 여부 설정 (나중에 실제 인증 정보로 대체)
        boolean isStaff = true;  // 테스트용
        
        inquiryService.addResponse(id, content, isStaff);
        return "redirect:/inquiry/" + id;
    }

    // 문의 처리완료 상태로 변경
    @PostMapping("/inquiry/{id}/complete")
    public String completeInquiry(@PathVariable Long id) {
        // 작시로 작성자 여부 설정 (나중에 실제 인증 정보로 대체)
        boolean isAuthor = true;  // 테스트용
        
        inquiryService.completeInquiry(id, isAuthor);
        return "redirect:/inquiry/" + id;
    }
}