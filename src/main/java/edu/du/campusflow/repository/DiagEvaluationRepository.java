package edu.du.campusflow.repository;

import edu.du.campusflow.dto.DiagEvaluationDetailDTO;
import edu.du.campusflow.entity.Ofregistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiagEvaluationRepository extends JpaRepository<Ofregistration, Long> {
    @Query("SELECT new edu.du.campusflow.dto.DiagEvaluationDetailDTO(" +
            "l.lectureName, " +
            "dq.questionName, " +
            "di.score, " +
            "m.name, " +
            "m.grade, " +  // semester 대신 member의 grade 사용
            "cs.subject.subjectId) " +
            "FROM DiagItem di " +
            "JOIN di.ofRegistration o " +
            "JOIN o.lectureId l " +
            "JOIN o.member m " +  // member 조인 추가
            "JOIN di.diagQuestion dq " +
            "JOIN l.curriculumSubject cs " +
            "JOIN cs.curriculum c " +
            "WHERE c.dept.deptId = :deptId " +
            "AND m.grade = :gradeCodeId " +  // semester 대신 member의 grade로 검색
            "AND (:lectureName IS NULL OR l.lectureName LIKE %:lectureName%) " +
            "AND (:studentName IS NULL OR m.name LIKE %:studentName%) " +
            "ORDER BY l.lectureName, dq.questionName")
    List<DiagEvaluationDetailDTO> findEvaluations(
            @Param("deptId") Long deptId,
            @Param("gradeCodeId") Long gradeCodeId,
            @Param("lectureName") String lectureName,
            @Param("studentName") String studentName
    );
}
