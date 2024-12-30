package edu.du.campusflow.repository;

import edu.du.campusflow.entity.DiagFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiagFeedbackRepository extends JpaRepository<DiagFeedback, Long> {
//    DiagFeedback findByOfRegistration_Id(Long ofregistrationId);
}