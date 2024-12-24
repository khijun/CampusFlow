package edu.du.campusflow.repository;

import edu.du.campusflow.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
} 