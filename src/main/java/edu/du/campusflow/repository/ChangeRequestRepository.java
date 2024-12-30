package edu.du.campusflow.repository;


import edu.du.campusflow.entity.ChangeRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChangeRequestRepository extends JpaRepository<ChangeRequest,Long> {
}
