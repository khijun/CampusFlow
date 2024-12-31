package edu.du.campusflow.repository;

import edu.du.campusflow.entity.ChangeHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChangeHistoryRepository extends JpaRepository<ChangeHistory,Long> {
    Optional<ChangeHistory> findByStudentId(Long id);
}
