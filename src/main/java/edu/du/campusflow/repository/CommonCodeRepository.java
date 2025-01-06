package edu.du.campusflow.repository;

import edu.du.campusflow.entity.CommonCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CommonCodeRepository extends JpaRepository<CommonCode, Long> {
    Optional<CommonCode> findByCodeValueAndCodeGroup_GroupCode(String codeValue, String groupCode);
} 