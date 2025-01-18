package edu.du.campusflow.controller;

import edu.du.campusflow.dto.MemberSearchFilter;
import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.service.CommonCodeGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/iframe/member")
public class MemberController {
    private final CommonCodeGroupService commonCodeGroupService;

    @GetMapping("/select_member")
    public String selectMember(Model model) {
        List<CommonCode> typeList = commonCodeGroupService.findByGroupCode("MEMBERTYPE").getCommonCodes();

        model.addAttribute("filter", MemberSearchFilter.builder().build());
        model.addAttribute("typeList", typeList);
        return "view/iframe/member/select_member";
    }

    @GetMapping("/create_member")
    public String createMember(Model model) {
        model.addAttribute("memberTypes", commonCodeGroupService.findByGroupCode("MEMBERTYPE").getCommonCodes());
        return "view/iframe/member/management/create_member";
    }
}
