package edu.du.campusflow.controller;

import edu.du.campusflow.entity.Inquiry;
import edu.du.campusflow.service.InquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class InquiryController {

    @Autowired
    private InquiryService inquiryService;

    // 문의 추가 페이지
    @GetMapping("/inquiry/add")
    public String showAddInquiryForm() {
        return "inquiry/addInquiry"; // 문의 추가 페이지로 이동
    }

    // 문의 추가 처리
    @PostMapping("/inquiry")
    public String addInquiry(Inquiry inquiry) {
        inquiryService.createInquiry(inquiry); // 문의 생성
        return "redirect:/inquiry/view"; // 문의 추가 후 목록 페이지로 리다이렉트
    }

    // 모든 문의 조회 페이지
    @GetMapping("/inquiry/view")
    public String viewInquiries(Model model) {
        model.addAttribute("inquiries", inquiryService.getAllInquiries()); // 모든 문의를 모델에 추가
        return "inquiry/viewInquiries"; // 문의 목록 페이지로 이동
    }

    // 특정 문의 상세 조회 페이지
    @GetMapping("/inquiry/{id}")
    public String viewInquiryDetail(@PathVariable Long id, Model model) {
        Inquiry inquiry = inquiryService.getInquiryById(id);
        model.addAttribute("inquiry", inquiry); // 특정 문의를 모델에 추가
        return "inquiry/inquiryDetail"; // 문의 상세 페이지로 이동
    }
}