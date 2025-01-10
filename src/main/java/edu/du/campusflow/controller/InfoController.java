package edu.du.campusflow.controller;

import edu.du.campusflow.entity.EducationInfo;
import edu.du.campusflow.entity.FamilyInfo;
import edu.du.campusflow.entity.MilitaryInfo;
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
        return "redirect:/iframe/info/education_student";
    }

    @PostMapping("/iframe/info/education_student/delete")
    public String deleteEducationInfo(@ModelAttribute EducationInfo educationInfo) {

        infoService.deleteEducationInfo(educationInfo.getId());
        return "redirect:/iframe/info/education_student";
    }

    @GetMapping("/iframe/info/family_student")
    public String family_student(Model model) {
        List<FamilyInfo> familyInfos = infoService.getFamilyInfoById(authService.getCurrentMemberId());
        model.addAttribute("familyInfos", familyInfos);
        return "view/iframe/info/family_student";
    }

    @PostMapping("/iframe/info/family_student/insert")
    public String insertFamilyInfo(@ModelAttribute FamilyInfo familyInfo) {
        Long memberId = authService.getCurrentMemberId();
        if (memberId == null) {
            throw new IllegalStateException("Authentication required to perform this action.");
        }
        infoService.saveFamilyInfo(familyInfo, memberId);
        return "redirect:/iframe/info/family_student";
    }

    @PostMapping("/iframe/info/family_student/delete")
    public String deleteFamilyInfo(@ModelAttribute FamilyInfo familyInfo) {
        infoService.deleteFamilyInfo(familyInfo.getId());
        return "redirect:/iframe/info/family_student";
    }

    @GetMapping("/iframe/info/military_student")
    public String military_student(Model model) {
        List<MilitaryInfo> militaryInfos = infoService.getMilitaryInfoById(authService.getCurrentMemberId());
        model.addAttribute("militaryInfos", militaryInfos);
        return "view/iframe/info/military_student";
    }

    @PostMapping("/iframe/info/military_student/insert")
    public String insertMilitaryInfo(@ModelAttribute MilitaryInfo militaryInfo) {
        Long memberId = authService.getCurrentMemberId();
        if (memberId == null) {
            throw new IllegalStateException("Authentication required to perform this action.");
        }
        infoService.saveMilitaryInfo(militaryInfo, memberId);
        return "redirect:/iframe/info/military_student";
    }

    @PostMapping("/iframe/info/military_student/delete")
    public String deleteMilitaryInfo(@ModelAttribute MilitaryInfo militaryInfo) {
        infoService.deleteMilitaryInfo(militaryInfo.getId());
        return "redirect:/iframe/info/military_student";
    }
}
