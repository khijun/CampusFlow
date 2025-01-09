package edu.du.campusflow.repository;

import edu.du.campusflow.entity.EducationInfo;
import edu.du.campusflow.entity.FamilyInfo;
import edu.du.campusflow.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FamilyInfoRepository extends JpaRepository<FamilyInfo,Long> {
    List<FamilyInfo> findByMember_MemberId(Long memberId);
}
