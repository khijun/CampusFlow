package edu.du.campusflow.repository;

import edu.du.campusflow.dto.LecQuestionDTO;
import edu.du.campusflow.entity.LecQuestion;
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
public interface LecQuestionRepository extends JpaRepository<Ofregistration, Long> {

    // lec 강의평가 결과 및 통계 쿼리
    @Query("SELECT new edu.du.campusflow.dto.LecQuestionDTO(" +
            "l.lectureName, " +
            "lq.questionName, " +
            "CAST(AVG(li.score) AS double), " +
            "m.name, " +
            "SUBSTRING(cc.codeValue, 7), " +  // GRADE_ 제거
            "cs.subject.subjectId, " +
            "m.memberId, " +
            "COUNT(CASE WHEN li.score = 5 THEN 1 END), " +
            "COUNT(CASE WHEN li.score = 4 THEN 1 END), " +
            "COUNT(CASE WHEN li.score = 3 THEN 1 END), " +
            "COUNT(CASE WHEN li.score = 2 THEN 1 END), " +
            "COUNT(CASE WHEN li.score = 1 THEN 1 END), " +
            "CAST(COUNT(CASE WHEN li.score = 5 THEN 1 END) * 100.0 / COUNT(*) AS double), " +
            "CAST(COUNT(CASE WHEN li.score = 4 THEN 1 END) * 100.0 / COUNT(*) AS double), " +
            "CAST(COUNT(CASE WHEN li.score = 3 THEN 1 END) * 100.0 / COUNT(*) AS double), " +
            "CAST(COUNT(CASE WHEN li.score = 2 THEN 1 END) * 100.0 / COUNT(*) AS double), " +
            "CAST(COUNT(CASE WHEN li.score = 1 THEN 1 END) * 100.0 / COUNT(*) AS double)) " +
            "FROM LecItem li " +
            "JOIN li.ofRegistration o " +
            "JOIN o.member m " +
            "JOIN o.lectureId l " +
            "JOIN li.lecQuestion lq " +
            "JOIN l.curriculumSubject cs " +
            "JOIN m.grade cc " +
            "WHERE m.dept.deptId = :deptId " +
            "AND m.grade.codeId = :gradeCodeId " +
            "AND (:lectureName IS NULL OR l.lectureName LIKE %:lectureName%) " +
            "AND (:name IS NULL OR m.name LIKE %:name%) " +
            "GROUP BY l.lectureName, lq.questionName, m.name, cc.codeValue, cs.subject.subjectId, m.memberId " +
            "ORDER BY m.name, l.lectureName, lq.questionName")
    List<LecQuestionDTO> findEvaluations(
            @Param("deptId") Long deptId,
            @Param("gradeCodeId") Long gradeCodeId,
            @Param("lectureName") String lectureName,
            @Param("name") String name);

    @Query("SELECT lq FROM LecQuestion lq WHERE lq.questionId = :questionId")
    Optional<LecQuestion> findLecQuestionById(@Param("questionId") Long questionId);

    // 교수가 담당하는 과목 불러오기
    @Query("SELECT l FROM Lecture l WHERE l.member.memberId = :professorId ORDER BY l.lectureName")
    @EntityGraph(attributePaths = "member")
    List<Lecture> findLecLecturesByProfessorId(@Param("professorId") Long professorId);

    @Query("SELECT lq FROM LecQuestion lq")
    List<LecQuestion> findAllLecQuestions();

}