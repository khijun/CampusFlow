package edu.du.campusflow.repository;

import edu.du.campusflow.dto.SubjectSearchFilter;
import edu.du.campusflow.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

   // 과목명에 특정 문자열이 포함된 과목 검색
   List<Subject> findBySubjectNameContaining(String keyword);

   @Query("SELECT s FROM Subject s WHERE " +
       "(:#{#filter.subjectName} IS NULL OR s.subjectName LIKE %:#{#filter.subjectName}%) " +
       "AND (:#{#filter.subjectCredits} IS NULL OR s.subjectCredits = :#{#filter.subjectCredits})")
   List<Subject> findAllWithFilter(SubjectSearchFilter filter);
}
