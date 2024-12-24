package edu.du.campusflow.repository;

import edu.du.campusflow.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
} 