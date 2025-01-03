package edu.du.campusflow.repository;

import edu.du.campusflow.entity.ChangeHistory;
import edu.du.campusflow.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChangeHistoryRepository extends JpaRepository<ChangeHistory,Long> {

    List<ChangeHistory> findByMember(Member memberId);
}
