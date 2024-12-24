package edu.du.campusflow.repository;

import edu.du.campusflow.entity.CommonCodeGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommonCodeGroupRepository extends JpaRepository<CommonCodeGroup, Long> {
    public CommonCodeGroup findByGroupCode(String code);
} 