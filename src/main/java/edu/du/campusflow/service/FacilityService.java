package edu.du.campusflow.service;

import edu.du.campusflow.dto.FacilityDTO;
import edu.du.campusflow.dto.LectureDTO;
import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.CommonCodeGroup;
import edu.du.campusflow.entity.Facility;
import edu.du.campusflow.entity.Lecture;
import edu.du.campusflow.repository.CommonCodeGroupRepository;
import edu.du.campusflow.repository.CommonCodeRepository;
import edu.du.campusflow.repository.FacilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacilityService {

    @Autowired
    FacilityRepository facilityRepository;

    @Autowired
    CommonCodeRepository commonCodeRepository;

    @Autowired
    CommonCodeGroupRepository commonCodeGroupRepository;


    //건물명 드롭다운에 건물 데이터 불러오는 서비스
    public List<CommonCode> getFacilityBuilding() {
        CommonCodeGroup buildingGroup = commonCodeGroupRepository.findByGroupCode("Building");
        System.out.println("buildingGroup: " + buildingGroup);

        if (buildingGroup == null) {
            return Collections.emptyList();
        }

        List<CommonCode> buildingCodes = commonCodeRepository.findByCodeGroup(buildingGroup);
        System.out.println("Found status codes: " + buildingCodes.size());
        buildingCodes.forEach(code ->
                System.out.println("Code: " + code.getCodeValue() + " - " + code.getCodeName())
        );
        return buildingCodes;
    }

    //강의실 상태 드롭다운에 데이터 불러오는 서비스
    public List<CommonCode> getFacilityStatus() {
        CommonCodeGroup facilityStatusGroup = commonCodeGroupRepository.findByGroupCode("FacilityStatus");
        System.out.println("facilityStatusGroup: " + facilityStatusGroup);

        if (facilityStatusGroup == null) {
            return Collections.emptyList();
        }

        List<CommonCode> facilityStatusCode = commonCodeRepository.findByCodeGroup(facilityStatusGroup);
        System.out.println("Found status codes: " + facilityStatusCode.size());
        facilityStatusCode.forEach(code ->
                System.out.println("Code: " + code.getCodeValue() + " - " + code.getCodeName())
        );
        return facilityStatusCode;
    }

    public List<FacilityDTO> getFacilityList(String facilityStatus, String building) {
        List<Facility> facilities = facilityRepository.findAll((root, query, criteriaBuilder) -> {
            // 건물과 상태 모두 있는 경우
            if (building != null) {
                if(facilityStatus != null && !facilityStatus.isEmpty()) {
                    return criteriaBuilder.and(
                            criteriaBuilder.equal(root.get("facilityStatus").get("codeValue"), facilityStatus),
                            criteriaBuilder.equal(root.get("building").get("codeValue"), building)
                    );
                }
                // 건물만 있는 경우
                return criteriaBuilder.equal(root.get("building").get("codeValue"), building);
            }
            // 상태만 있는 경우
            if(facilityStatus != null && !facilityStatus.isEmpty()) {
                return criteriaBuilder.equal(root.get("facilityStatus").get("codeValue"), facilityStatus);
            }
            // 아무 조건도 없는 경우 - 전체 조회
            return criteriaBuilder.conjunction();
        });

        return facilities.stream()
                .map(facility -> {
                    FacilityDTO dto = new FacilityDTO();
                    dto.setFacilityId(facility.getFacilityId());
                    dto.setFacilityName(facility.getFacilityName());
                    dto.setCapacity(facility.getCapacity());
                    dto.setBuilding(facility.getBuilding().getCodeName());
                    dto.setFacilityStatus(facility.getFacilityStatus().getCodeName());
                    dto.setFloor(facility.getFloor().getCodeName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

}
