package edu.du.campusflow.repository;

import edu.du.campusflow.entity.DiagQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiagQuestionRepository extends JpaRepository<DiagQuestion, Long> {
    // 문항 이름으로 검색
    List<DiagQuestion> findByQuestionNameContaining(String keyword);
}