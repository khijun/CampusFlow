package edu.du.campusflow.repository;

import edu.du.campusflow.entity.DiagQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiagQuestionRepository extends JpaRepository<DiagQuestion, Long> {
}