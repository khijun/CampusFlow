package edu.du.campusflow.repository;

import edu.du.campusflow.entity.Assignment;
import edu.du.campusflow.entity.Ofregistration;
import edu.du.campusflow.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    Optional<Submission> findByAssignmentAndOfregistration(Assignment assignment, Ofregistration ofregistration);

    @Query("SELECT COUNT(s) FROM Submission s WHERE s.assignment = :assignment")
    Integer countByAssignment(@Param("assignment") Assignment assignment);

    // SubmissionRepository에 추가
    @Query("SELECT s FROM Submission s " +
            "JOIN FETCH s.ofregistration o " +
            "JOIN FETCH o.member m " +
            "JOIN FETCH m.dept " +
            "WHERE s.assignment = :assignment")
    List<Submission> findByAssignment(@Param("assignment") Assignment assignment);
}
