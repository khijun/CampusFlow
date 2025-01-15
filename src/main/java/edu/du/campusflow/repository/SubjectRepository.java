package edu.du.campusflow.repository;

import edu.du.campusflow.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

   // 과목명에 특정 문자열이 포함된 과목 검색
   List<Subject> findBySubjectNameContaining(String keyword);
}
