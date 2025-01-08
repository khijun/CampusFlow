package edu.du.campusflow.controller;

import edu.du.campusflow.entity.EducationInfo;
import edu.du.campusflow.repository.EducationInfoRepository;
import edu.du.campusflow.repository.FamilyInfoRepository;
import edu.du.campusflow.repository.MilitaryInfoRepository;
import edu.du.campusflow.service.AuthService;
import edu.du.campusflow.service.InfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class InfoController {
    private final InfoService infoService;
    private final AuthService authService;
    private final EducationInfoRepository educationInfoRepository;
    private final FamilyInfoRepository familyInfoRepository;
    private final MilitaryInfoRepository militaryInfoRepository;

    @GetMapping("/iframe/info/education_student")
    public String edu_infoStudent(Model model) {
        List<EducationInfo> educationInfos = infoService.getEducationInfoById(authService.getCurrentMemberId());
        model.addAttribute("educationInfos", educationInfos);
        return "view/iframe/info/education_student";
    }

    @PostMapping("/iframe/info/education_student/insert")
    public String insertEducationInfo(@ModelAttribute EducationInfo educationInfo) {
        Long memberId = authService.getCurrentMemberId();
        if (memberId == null) {
            throw new IllegalStateException("Authentication required to perform this action.");
        }
        infoService.saveEducationInfo(educationInfo, memberId);
        return "redirect:/view/iframe/info/education_student";
    }
}
