package edu.du.campusflow.repository;

import edu.du.campusflow.entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacilityRepository extends JpaRepository<Facility, Long> {
}
