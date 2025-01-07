package edu.du.campusflow.repository;

import edu.du.campusflow.entity.ChangeHistory;
import edu.du.campusflow.entity.ChangeRequest;
import edu.du.campusflow.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChangeHistoryRepository extends JpaRepository<ChangeHistory,Long> {
    // Member의 memberId로 조회
    List<ChangeHistory> findByMember_MemberId(Long memberId);

    
}
