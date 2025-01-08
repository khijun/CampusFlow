package edu.du.campusflow.controller;

import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.Facility;
import edu.du.campusflow.repository.CommonCodeRepository;
import edu.du.campusflow.repository.FacilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class FacilityController {

    @Autowired
    FacilityRepository facilityRepository;

    @Autowired
    CommonCodeRepository commonCodeRepository;

    @GetMapping("/api/classrooms/{buildingCode}")
    @ResponseBody
    public List<Facility> getClassrooms(@PathVariable String buildingCode) {
        CommonCode building = commonCodeRepository.findByCodeValue(buildingCode);
        return facilityRepository.findByBuilding(building);
    }
}
