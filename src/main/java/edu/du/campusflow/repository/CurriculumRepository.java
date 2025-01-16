package edu.du.campusflow.repository;

import edu.du.campusflow.entity.Curriculum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CurriculumRepository extends JpaRepository<Curriculum, Long>, JpaSpecificationExecutor<Curriculum> {
}
