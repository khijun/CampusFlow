package edu.du.campusflow.repository;


import edu.du.campusflow.entity.ChangeRequest;
import edu.du.campusflow.entity.CommonCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChangeRequestRepository extends JpaRepository<ChangeRequest,Long> {
    // Member의 memberId로 조회
    List<ChangeRequest> findByMember_MemberId(Long memberId);
}
