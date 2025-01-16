package edu.du.campusflow.repository;

import edu.du.campusflow.entity.DiagQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiagQuestionRepository extends JpaRepository<DiagQuestion, Long> {
    @Query("SELECT DISTINCT dq FROM DiagQuestion dq " +
            "JOIN DiagItem di ON di.diagQuestion = dq " +
            "WHERE di.ofRegistration.id = :ofregistrationId")
    List<DiagQuestion> findByOfRegistration_Id(@Param("ofregistrationId") Long ofregistrationId);
}