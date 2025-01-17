package edu.du.campusflow.controller;

import edu.du.campusflow.dto.MemberSearchFilter;
import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.repository.CommonCodeGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final CommonCodeGroupRepository commonCodeGroupRepository;

    @GetMapping("/iframe/member/select_member")
    public String selectMember(Model model) {
        List<CommonCode> typeList = commonCodeGroupRepository.findByGroupCode("MEMBERTYPE").getCommonCodes();

        model.addAttribute("filter", MemberSearchFilter.builder().build());
        model.addAttribute("typeList", typeList);
        return "view/iframe/member/select_member";
    }
}
