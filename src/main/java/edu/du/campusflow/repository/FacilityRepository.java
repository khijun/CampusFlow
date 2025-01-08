package edu.du.campusflow.repository;

import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacilityRepository extends JpaRepository<Facility, Long> {
    List<Facility> findByBuilding(CommonCode building);
}
