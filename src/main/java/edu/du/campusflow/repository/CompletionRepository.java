package edu.du.campusflow.repository;

import edu.du.campusflow.entity.Completion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompletionRepository extends JpaRepository<Completion, Long> {
}
