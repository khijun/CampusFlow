package edu.du.academic_management_system.repository;

import edu.du.academic_management_system.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRepository extends JpaRepository<Staff, Long> {
} 