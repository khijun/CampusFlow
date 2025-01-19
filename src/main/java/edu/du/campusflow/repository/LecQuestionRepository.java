package edu.du.campusflow.repository;

import edu.du.campusflow.dto.LecQuestionDTO;
import edu.du.campusflow.entity.LecQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LecQuestionRepository extends JpaRepository<LecQuestion, Long> {

    @Query("SELECT new edu.du.campusflow.dto.LecQuestionDTO(" +
            "l.lectureName, " +
            "lq.questionName, " +
            "CAST(AVG(li.score) AS double), " +
            "m.name, " +
            "cc.codeValue, " +
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
}