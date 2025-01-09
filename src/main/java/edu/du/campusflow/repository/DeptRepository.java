package edu.du.campusflow.repository;

import edu.du.campusflow.entity.Dept;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeptRepository extends JpaRepository<Dept, Long> {
}
