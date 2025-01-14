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
            "cc.codeValue, " +
            "cs.subject.subjectId, " +
            "m.memberId) " +  // memberId 추가
            "FROM DiagItem di " +
            "JOIN di.ofRegistration o " +
            "JOIN o.member m " +
            "JOIN o.lectureId l " +
            "JOIN di.diagQuestion dq " +
            "JOIN l.curriculumSubject cs " +
            "JOIN m.grade cc " +
            "WHERE m.dept.deptId = :deptId " +
            "AND m.grade.codeId = :gradeCodeId " +
            "AND (:lectureName IS NULL OR l.lectureName LIKE %:lectureName%) " +
            "AND (:name IS NULL OR m.name LIKE %:name%) " +
            "ORDER BY m.name, l.lectureName, dq.questionName")  // 학생명, 강의명, 문항 순으로 정렬
    List<DiagEvaluationDetailDTO> findEvaluations(
            @Param("deptId") Long deptId,
            @Param("gradeCodeId") Long gradeCodeId,
            @Param("lectureName") String lectureName,
            @Param("name") String name);
}
