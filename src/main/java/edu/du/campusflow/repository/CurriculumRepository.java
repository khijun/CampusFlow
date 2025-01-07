package edu.du.campusflow.repository;

import edu.du.campusflow.entity.Curriculum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurriculumRepository extends JpaRepository<Curriculum, Long> {
}
