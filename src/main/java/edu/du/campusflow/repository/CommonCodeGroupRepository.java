package edu.du.campusflow.repository;

import edu.du.campusflow.entity.CommonCodeGroup;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommonCodeGroupRepository extends JpaRepository<CommonCodeGroup, Long> {
    @EntityGraph(attributePaths = {"commonCodes"})
    public CommonCodeGroup findByGroupCode(String code);
} 