package edu.du.campusflow.repository;

import edu.du.campusflow.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

   List<Subject> findBySubjectNameContaining(String keyword);

}
