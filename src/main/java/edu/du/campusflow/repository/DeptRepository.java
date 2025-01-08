package edu.du.campusflow.repository;

import edu.du.campusflow.entity.Dept;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeptRepository extends JpaRepository<Dept, Long> {
   List<Dept> findByDeptNameContaining(String deptName);
}
