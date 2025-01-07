package edu.du.campusflow.controller;

import edu.du.campusflow.dto.TuitionDTO;
import edu.du.campusflow.entity.Member;
import edu.du.campusflow.service.TuitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/iframe/tuition")
@RequiredArgsConstructor
public class TuitionController {

    private final TuitionService tuitionService;

    /**
     * 등록금 대상자 관리 페이지를 보여줍니다.
     */
    @GetMapping("/admin/tuitionTarget")
    public String showTuitionTargetPage(Model model, @SessionAttribute("student") Member student) {
        model.addAttribute("tuitionTargets", tuitionService.getTuitionInfo(student));
        return "view/iframe/tuition/admin/tuitionTarget";
    }

}
