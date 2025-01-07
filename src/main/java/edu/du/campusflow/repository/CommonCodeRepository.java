package edu.du.campusflow.repository;

import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.CommonCodeGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommonCodeRepository extends JpaRepository<CommonCode, Long> {
    public CommonCode findByCodeValue(String code);
    public List<CommonCode> findByCodeGroup(CommonCodeGroup codeGroup);
} 