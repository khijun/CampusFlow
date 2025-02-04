package edu.du.campusflow.controller;

import edu.du.campusflow.dto.FacilityDTO;
import edu.du.campusflow.dto.LectureTimeDTO;
import edu.du.campusflow.repository.CommonCodeRepository;
import edu.du.campusflow.repository.FacilityRepository;
import edu.du.campusflow.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class FacilityController {

    @Autowired
    FacilityRepository facilityRepository;

    @Autowired
    CommonCodeRepository commonCodeRepository;

    @Autowired
    FacilityService facilityService;

    @GetMapping("/iframe/facility/facilityList")
    public String facilityList(Model model) {
        model.addAttribute("building", facilityService.getFacilityBuilding());
        model.addAttribute("facilityStatus", facilityService.getFacilityStatus());
        return "view/iframe/facility/facility_List";
    }

    //건물명 선택시 건물에 맞는 강의실 목록 드롭다운에 추가하는 컨트롤러
    @GetMapping("/api/facility/{buildingCode}")
    @ResponseBody
    public ResponseEntity<?> getClassrooms(@PathVariable String buildingCode) {
        try {
            return ResponseEntity.ok(facilityService.getClassrooms(buildingCode));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //강의실 리스트 조회
    @GetMapping("/api/facility/search")
    @ResponseBody
    public List<FacilityDTO> searchFacilities(
            @RequestParam(required = false) String building,
            @RequestParam(required = false) String facilityStatus) {
        return facilityService.getFacilityList(facilityStatus, building);
    }

    //강의실 상태 변경하는 컨트롤러
    @PostMapping("/api/facility/update-status")
    @ResponseBody
    public ResponseEntity<String> updateFacilityStatus(
            @RequestParam Long facilityId,
            @RequestParam String facilityStatus) {
        try {
            facilityService.updateFacilityStatus(facilityId, facilityStatus);
            return ResponseEntity.ok("강의실 상태가 성공적으로 변경되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/iframe/facility/facilityLecture_List")
    public String facilityLectureList(Model model) {
        model.addAttribute("building", facilityService.getFacilityBuilding());
        model.addAttribute("facility", facilityService.getFacilityStatus());
        return "view/iframe/facility/facilityLecture_List";
    }

    @GetMapping("/api/facility/lectures/{facilityId}")
    @ResponseBody
    public ResponseEntity<List<LectureTimeDTO>> getFacilityLectures(@PathVariable Long facilityId) {
        try {
            List<LectureTimeDTO> lectures = facilityService.getFacilityLectures(facilityId);
            // 디버깅을 위한 로그
            System.out.println("Controller - Found lectures: " + lectures.size());
            return ResponseEntity.ok(lectures);
        } catch (Exception e) {
            System.err.println("Error in getFacilityLectures: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

}
