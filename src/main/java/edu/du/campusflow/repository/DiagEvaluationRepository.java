package edu.du.campusflow.repository;

import edu.du.campusflow.dto.DiagEvaluationDetailDTO;
import edu.du.campusflow.entity.DiagQuestion;
import edu.du.campusflow.entity.Lecture;
import edu.du.campusflow.entity.Ofregistration;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiagEvaluationRepository extends JpaRepository<Ofregistration, Long> {
    @Query("SELECT new edu.du.campusflow.dto.DiagEvaluationDetailDTO(" +
            "l.lectureName, " +
            "dq.questionName, " +
            "dq.questionId, " +
            "di.score, " +
            "m.name, " +
            "SUBSTRING(cc.codeValue, 7), " +
            "cs.subject.subjectId, " +
            "m.memberId, " +
            "CAST((SELECT AVG(CAST(di2.score AS double)) FROM DiagItem di2 " +  // Double로 캐스팅
            "WHERE di2.ofRegistration = di.ofRegistration " +
            "AND di2.diagQuestion = di.diagQuestion) AS double)) " +
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
            "ORDER BY m.name, l.lectureName, dq.questionName")
    List<DiagEvaluationDetailDTO> findEvaluations(
            @Param("deptId") Long deptId,
            @Param("gradeCodeId") Long gradeCodeId,
            @Param("lectureName") String lectureName,
            @Param("name") String name);

    @Query("SELECT dq FROM DiagQuestion dq WHERE dq.questionId = :questionId")
    Optional<DiagQuestion> findDiagQuestionById(@Param("questionId") Long questionId);

    // 교수가 담당하는 과목 불러오기
    @Query("SELECT l FROM Lecture l " +
            "WHERE l.member.memberId = :professorId " +
            "ORDER BY l.lectureName")
    @EntityGraph(attributePaths = "member")
    List<Lecture> findDiagLecturesByProfessorId(@Param("professorId") Long professorId);
}
