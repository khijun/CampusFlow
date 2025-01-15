package edu.du.campusflow.controller;

import edu.du.campusflow.service.CommonCodeGroupService;
import edu.du.campusflow.service.DeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DeptController {
    private final DeptService deptService;
    private final CommonCodeGroupService commonCodeGroupService;

    @GetMapping("/iframe/dept/view")
    public String deptView(Model model) {
        model.addAttribute("deptStatus", commonCodeGroupService.findByGroupCode("DEPTSTATUS").getCommonCodes());
        return "view/iframe/dept/dept_view";
    }

}
