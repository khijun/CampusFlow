package edu.du.campusflow.repository;

import edu.du.campusflow.entity.EducationInfo;
import edu.du.campusflow.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EducationInfoRepository extends JpaRepository<EducationInfo,Long> {
    @EntityGraph(attributePaths = {"graduationStatus"})
    List<EducationInfo> findByMember_MemberId(Long memberId);
}
