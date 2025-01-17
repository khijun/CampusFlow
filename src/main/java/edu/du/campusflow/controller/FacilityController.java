package edu.du.campusflow.controller;

import edu.du.campusflow.dto.FacilityDTO;
import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.Facility;
import edu.du.campusflow.repository.CommonCodeRepository;
import edu.du.campusflow.repository.FacilityRepository;
import edu.du.campusflow.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
        return "/view/iframe/facility/facility_List";
    }

    @GetMapping("/api/facility/{buildingCode}")
    @ResponseBody
    public List<Facility> getClassrooms(@PathVariable String buildingCode) {
        CommonCode building = commonCodeRepository.findByCodeValue(buildingCode);
        return facilityRepository.findByBuilding(building);
    }

    @GetMapping("/api/facility/search")
    @ResponseBody
    public List<FacilityDTO> searchFacilities(
            @RequestParam(required = false) String building,
            @RequestParam(required = false) String facilityStatus) {
        return facilityService.getFacilityList(facilityStatus, building);
    }
}
