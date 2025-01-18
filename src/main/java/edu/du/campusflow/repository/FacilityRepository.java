package edu.du.campusflow.repository;

import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface FacilityRepository extends JpaRepository<Facility, Long> , JpaSpecificationExecutor<Facility> {
    List<Facility> findByBuilding(CommonCode building);
    List<Facility> findByBuildingAndFacilityStatus(CommonCode building, CommonCode facilityStatus);
}
