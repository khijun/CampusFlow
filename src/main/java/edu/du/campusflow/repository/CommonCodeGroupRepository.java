package edu.du.campusflow.repository;

import edu.du.campusflow.entity.CommonCodeGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommonCodeGroupRepository extends JpaRepository<CommonCodeGroup, Long> {
    Optional<CommonCodeGroup> findByGroupCode(String groupCode);
} 