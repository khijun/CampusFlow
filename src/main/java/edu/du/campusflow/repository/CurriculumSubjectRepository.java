package edu.du.campusflow.repository;

import edu.du.campusflow.entity.CurriculumSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CurriculumSubjectRepository extends JpaRepository<CurriculumSubject, Long>, JpaSpecificationExecutor<CurriculumSubject> {
}