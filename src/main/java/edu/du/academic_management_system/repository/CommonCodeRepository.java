package edu.du.academic_management_system.repository;

import edu.du.academic_management_system.entity.CommonCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommonCodeRepository extends JpaRepository<CommonCode, Long> {
    public CommonCode findByCodeValue(String code);
} 