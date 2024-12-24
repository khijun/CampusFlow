package edu.du.campusflow.repository;

import edu.du.campusflow.entity.CommonCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommonCodeRepository extends JpaRepository<CommonCode, Long> {
    public CommonCode findByCodeValue(String code);
} 