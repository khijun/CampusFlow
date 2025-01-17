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
import org.springframework.transaction.annotation.Transactional;

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

    public List<Facility> getClassrooms(String buildingCode) {
        CommonCode building = commonCodeRepository.findByCodeValue(buildingCode);
        if (building == null) {
            throw new RuntimeException("해당 건물을 찾을 수 없습니다.");
        }
        CommonCode availableStatus = commonCodeRepository.findByCodeValue("AVAILABLE");
        if (availableStatus == null) {
            throw new RuntimeException("사용 가능 상태 코드를 찾을 수 없습니다.");
        }
        return facilityRepository.findByBuildingAndFacilityStatus(building, availableStatus);
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

    //강의실 상태를 변경하는 서비스
    @Transactional
    public void updateFacilityStatus(Long facilityId, String facilityStatus) {
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new RuntimeException("해당 강의실을 찾을 수 없습니다."));

        CommonCode status = commonCodeRepository.findByCodeValue(facilityStatus);
        if (status == null) {
            throw new RuntimeException("유효하지 않은 상태 코드입니다.");
        }

        facility.setFacilityStatus(status);
        facilityRepository.save(facility);
    }

}
