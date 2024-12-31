package edu.du.campusflow.repository;

import edu.du.campusflow.entity.LecQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LecQuestionRepository extends JpaRepository<LecQuestion, Long> {
    List<LecQuestion> findByQuestionNameContaining(String keyword);
}