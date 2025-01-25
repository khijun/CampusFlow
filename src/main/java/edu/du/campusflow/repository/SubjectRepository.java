package edu.du.campusflow.repository;

import edu.du.campusflow.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

   List<Subject> findBySubjectNameContaining(String keyword);

   @Query("SELECT s FROM Subject s WHERE CAST(s.subjectId AS string) LIKE CONCAT(:deptId, '%') ORDER BY s.subjectName ASC")
   List<Subject> findSubjectsByDeptId(@Param("deptId") String deptId);
}
