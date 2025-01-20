package edu.du.campusflow.repository;

import edu.du.campusflow.entity.Curriculum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CurriculumRepository extends JpaRepository<Curriculum, Long> {
   List<Curriculum> findByCurriculumNameContaining(String curriculumName);

}
