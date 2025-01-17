package edu.du.campusflow.controller;

import edu.du.campusflow.entity.EducationInfo;
import edu.du.campusflow.entity.FamilyInfo;
import edu.du.campusflow.entity.MilitaryInfo;
import edu.du.campusflow.service.AuthService;
import edu.du.campusflow.service.InfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class InfoToastController {

    private final InfoService infoService;
    private final AuthService authService;

    @GetMapping("/api/education_info")
    public ResponseEntity<List<EducationInfo>> infoToast() {
        List<EducationInfo> educationInfos = infoService.getEducationInfoById(authService.getCurrentMemberId());
        return ResponseEntity.ok(educationInfos);
    }

    @GetMapping("/api/family_info")
    public ResponseEntity<List<FamilyInfo>> familyInfoToast() {
        List<FamilyInfo> familyInfos = infoService.getFamilyInfoById(authService.getCurrentMemberId());
        return ResponseEntity.ok(familyInfos);
    }

    @GetMapping("/api/military_info")
    public ResponseEntity<List<MilitaryInfo>> militaryInfoToast() {
        List<MilitaryInfo> militaryInfos = infoService.getMilitaryInfoById(authService.getCurrentMemberId());
        return ResponseEntity.ok(militaryInfos);
    }
}
