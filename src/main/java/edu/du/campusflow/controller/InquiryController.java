package edu.du.campusflow.controller;

import edu.du.campusflow.entity.Inquiry;
import edu.du.campusflow.service.InquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.AccessDeniedException;

@Controller
@RequestMapping("/iframe/inquiry")
public class InquiryController {

    @Autowired
    private InquiryService inquiryService;

    // 문의 추가 페이지
    @GetMapping("/add")
    public String showAddInquiryForm() {
        // 학생이 아닌 경우 접근 거부
        if (!inquiryService.isStudent()) {
            throw new AccessDeniedException("학생만 문의사항을 작성할 수 있습니다.");
        }
        return "view/iframe/inquiry/addInquiry"; // 문의 추가 페이지로 이동
    }

    // 문의 추가 처리
    @PostMapping("/add")
    public String addInquiry(@ModelAttribute Inquiry inquiry) {
        inquiryService.createInquiry(inquiry);
        return "redirect:/iframe/inquiry/view";
    }

    // 모든 문의 조회 페이지
    @GetMapping("/view")
    public String viewInquiries(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Inquiry> inquiryPage = inquiryService.getInquiriesByUserRole(pageable);

        model.addAttribute("inquiryPage", inquiryPage);
        model.addAttribute("isStaff", inquiryService.isStaff());
        model.addAttribute("isStudent", inquiryService.isStudent());
        return "view/iframe/inquiry/viewInquiries";
    }

    // 특정 문의 상세 조회 페이지
    @GetMapping("detail-view/{id}")
    public String showInquiryDetail(@PathVariable Long id, Model model) {
        Inquiry inquiry = inquiryService.getInquiryById(id);
        
        // 작성자나 교직원이 아닌 경우 접근 거부
        if (!inquiryService.isAuthor(inquiry) && !inquiryService.isStaff()) {
            throw new AccessDeniedException("해당 문의사항에 대한 접근 권한이 없습니다.");
        }

        model.addAttribute("inquiry", inquiry);
        model.addAttribute("isAuthor", inquiryService.isAuthor(inquiry));
        model.addAttribute("isStaff", inquiryService.isStaff());

        return "view/iframe/inquiry/inquiryDetail";
    }
    @PostMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('STAFF')")
    public String completeInquiry(@PathVariable Long id) {
        try{
            inquiryService.completeInquiry(id);
            return "redirect:/iframe/inquiry/detail-view/" + id;
        }catch(IllegalStateException e){
            return "redirect:/iframe/inquiry/delete-view/" + id;
        }
    }
    @PostMapping("/{id}/reply")
    @PreAuthorize("hasAnyRole('STAFF')")
    public String addInquiryReply(@PathVariable Long id, Inquiry response) {
        try {
            inquiryService.addResponse(id, response);
            return "redirect:/iframe/inquiry/detail-view/" + id;
        }catch(IllegalStateException e){
            return "redirect:/iframe/inquiry/delete-view/" + id;
        }
    }
}