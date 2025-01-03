package edu.du.campusflow.repository;

import edu.du.campusflow.entity.CommonCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommonCodeRepository extends JpaRepository<CommonCode, Long> {
    CommonCode findByCodeId(Long id);
}