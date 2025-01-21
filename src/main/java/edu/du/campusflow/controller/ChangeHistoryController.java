package edu.du.campusflow.controller;

import edu.du.campusflow.dto.SimpleMemberDTO;
import edu.du.campusflow.entity.ChangeHistory;
import edu.du.campusflow.entity.Member;
import edu.du.campusflow.repository.ChangeHistoryRepository;
import edu.du.campusflow.repository.MemberRepository;
import edu.du.campusflow.service.ChangeHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChangeHistoryController {
    private final ChangeHistoryService changeHistoryService;
    private final MemberRepository memberRepository;


    @GetMapping("/iframe/academic/admin/history_view")
    public String historyView(Model model) {
        List<ChangeHistory> changeHistoryList = changeHistoryService.findAll();
        model.addAttribute("changeHistoryList", changeHistoryList);
        return "view/iframe/academic/admin/admin-change-history-list";
    }

    @GetMapping("/iframe/academic/admin/student_status_management")
    public String studentStatusManagement(Model model) {
        List<Member> members = changeHistoryService.getMembersWithType101();
        model.addAttribute("members", members);
        return "view/iframe/academic/admin/student-status-management";
    }

    @PostMapping("/members/{memberId}/expel")
    public String withdrawMember(@PathVariable Long memberId) {
        changeHistoryService.processExpulsionOrWithdrawal(memberId, false); // 제적 처리
        return "redirect:/iframe/academic/admin/student_status_management";
    }

    @PostMapping("/members/{memberId}/withdraw")
    public String expelMember(@PathVariable Long memberId) {
        changeHistoryService.processExpulsionOrWithdrawal(memberId, true); // 퇴학 처리
        return "redirect:/iframe/academic/admin/student_status_management";
    }
}
