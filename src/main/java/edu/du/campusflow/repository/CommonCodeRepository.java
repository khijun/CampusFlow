package edu.du.campusflow.repository;

import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.CommonCodeGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommonCodeRepository extends JpaRepository<CommonCode, Long> {
    public CommonCode findByCodeValue(String code);
    public List<CommonCode> findByCodeGroup(CommonCodeGroup codeGroup);

    // 특정 그룹 코드에 속한 공통 코드들을 조회하는 메서드 ^^7
    @Query("SELECT c FROM CommonCode c JOIN c.codeGroup g WHERE g.groupCode = :groupCode")
    List<CommonCode> findByGroupCode(@Param("groupCode") String groupCode);

    @Query("SELECT c FROM CommonCode c " +
            "WHERE c.codeGroup.groupCode LIKE :codeGroup " +
            "AND c.codeValue LIKE :codeValue ")
    CommonCode findByCodeGroupAndCodeValue(String codeGroup, String codeValue);

} 