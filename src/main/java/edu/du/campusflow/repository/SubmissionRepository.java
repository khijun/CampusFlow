package edu.du.campusflow.repository;

import edu.du.campusflow.entity.Assignment;
import edu.du.campusflow.entity.Ofregistration;
import edu.du.campusflow.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    Optional<Submission> findByAssignmentAndOfregistration(Assignment assignment, Ofregistration ofregistration);
}
