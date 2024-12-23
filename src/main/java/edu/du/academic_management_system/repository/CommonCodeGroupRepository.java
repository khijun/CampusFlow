package edu.du.academic_management_system.repository;

import edu.du.academic_management_system.entity.CommonCodeGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommonCodeGroupRepository extends JpaRepository<CommonCodeGroup, Long> {
    public CommonCodeGroup findByGroupCode(String code);
} 