package edu.du.campusflow.repository;

import edu.du.campusflow.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRepository extends JpaRepository<Staff, Long> {
} 