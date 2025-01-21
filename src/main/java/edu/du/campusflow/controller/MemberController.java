package edu.du.campusflow.controller;

import edu.du.campusflow.dto.DeptDTO;
import edu.du.campusflow.dto.MemberSearchFilter;
import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.service.CommonCodeGroupService;
import edu.du.campusflow.service.DeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final DeptService deptService;

    @GetMapping("/select_member")
    @PreAuthorize("hasAnyRole('STAFF', 'PROFESSOR')")
    public String selectMember(Model model) {
        List<CommonCode> typeList = commonCodeGroupService.findByGroupCode("MEMBERTYPE").getCommonCodes();

        model.addAttribute("filter", MemberSearchFilter.builder().build());
        model.addAttribute("deptList", DeptDTO.fromEntityList(deptService.findAll()));
        model.addAttribute("academicStatusList", commonCodeGroupService.findByGroupCode("ACADEMICSTATUS").getCommonCodes());
        model.addAttribute("typeList", typeList);
        return "view/iframe/member/select_member";
    }

    @GetMapping("/create_member")
    @PreAuthorize("hasAnyRole('STAFF')")
    public String createMember(Model model) {
        model.addAttribute("memberTypes", commonCodeGroupService.findByGroupCode("MEMBERTYPE").getCommonCodes());
        model.addAttribute("depts", deptService.findAll());
        return "view/iframe/member/management/create_member";
    }

    @GetMapping("/member_update")
    @PreAuthorize("isAuthenticated()")
    public String memberInfo() {
        return "view/iframe/member/student/member_update";
    }
}
