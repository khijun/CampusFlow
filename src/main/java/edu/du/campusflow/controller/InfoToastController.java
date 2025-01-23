package edu.du.campusflow.controller;

import edu.du.campusflow.entity.EducationInfo;
import edu.du.campusflow.entity.FamilyInfo;
import edu.du.campusflow.entity.MilitaryInfo;
import edu.du.campusflow.service.AuthService;
import edu.du.campusflow.service.InfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/api/family_info/save")
    public ResponseEntity<?> saveFamilyInfo(@RequestBody FamilyInfo familyInfo) {
        Long memberId = authService.getCurrentMemberId();
        if (memberId == null) {
            throw new IllegalStateException("Authentication required to perform this action.");
        }
        infoService.saveFamilyInfo(familyInfo, memberId);
        return ResponseEntity.ok().body("{\"message\": \"가족정보가 성공적으로 등록되었습니다.\"}");
    }

    @PostMapping("/api/military_info/save")
    public ResponseEntity<?> saveMilitaryInfo(@RequestBody MilitaryInfo militaryInfo) {
        Long memberId = authService.getCurrentMemberId();
        if (memberId == null) {
            throw new IllegalStateException("Authentication required to perform this action.");
        }
        infoService.saveMilitaryInfo(militaryInfo, memberId);
        return ResponseEntity.ok().body("{\"message\": \"병역정보가 성공적으로 등록되었습니다.\"}");
    }

    @PostMapping("/api/education_info/save")
    public ResponseEntity<?> saveEducationInfo(@RequestBody EducationInfo educationInfo) {
        Long memberId = authService.getCurrentMemberId();
        if (memberId == null) {
            throw new IllegalStateException("Authentication required to perform this action.");
        }
        infoService.saveEducationInfo(educationInfo, memberId);
        return ResponseEntity.ok().body("{\"message\": \"학력정보가 성공적으로 등록되었습니다.\"}");
    }

    @DeleteMapping("/api/family_info/delete")
    public ResponseEntity<?> deleteFamilyInfo(@RequestBody FamilyInfo familyInfo) {
        infoService.deleteFamilyInfo(familyInfo.getId());
        return ResponseEntity.ok().body("{\"message\": \"가족정보가 성공적으로 삭제되었습니다.\"}");
    }

    @DeleteMapping("/api/military_info/delete")
    public ResponseEntity<?> deleteMilitaryInfo(@RequestBody MilitaryInfo militaryInfo) {
        infoService.deleteMilitaryInfo(militaryInfo.getId());
        return ResponseEntity.ok().body("{\"message\": \"병역정보가 성공적으로 삭제되었습니다.\"}");
    }

    @DeleteMapping("/api/education_info/delete")
    public ResponseEntity<?> deleteEducationInfo(@RequestBody EducationInfo educationInfo) {
        infoService.deleteEducationInfo(educationInfo.getId());
        return ResponseEntity.ok().body("{\"message\": \"학력정보가 성공적으로 삭제되었습니다.\"}");
    }


}
