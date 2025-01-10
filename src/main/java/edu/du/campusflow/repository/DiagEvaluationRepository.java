package edu.du.campusflow.repository;

import edu.du.campusflow.dto.DiagEvaluationDetailDTO;
import edu.du.campusflow.dto.DiagEvaluationStatsDTO;
import edu.du.campusflow.entity.Ofregistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiagEvaluationRepository extends JpaRepository<Ofregistration, Long> {
    @Query(value = "SELECT new edu.du.campusflow.dto.DiagEvaluationStatsDTO(" +
            "s.subject_id, " +
            "cc.code_id, " +
            "l.lecture_name, " +
            "COUNT(o), " +
            "0.0) " +
            "FROM ofregistration o " +
            "JOIN lecture l ON o.lecture_id = l.lecture_id " +
            "JOIN curriculum_subject cs ON l.curriculum_subject_id = cs.curriculum_subject_id " +
            "JOIN subject s ON cs.subject_id = s.subject_id " +
            "JOIN common_code cc ON l.semester = cc.code_id " +
            "JOIN curriculum c ON cs.curriculum_id = c.curriculum_id " +
            "WHERE (:deptId IS NULL OR c.dept_id = :deptId) " +
            "AND cc.code_id = :gradeCodeId " +
            "AND l.lecture_name = :lectureName " +
            "GROUP BY s.subject_id, cc.code_id, l.lecture_name",
            nativeQuery = true)
    List<DiagEvaluationStatsDTO> findEvaluationStatsByLecture(
            @Param("deptId") Long deptId,
            @Param("gradeCodeId") Long gradeCodeId,
            @Param("lectureName") String lectureName);

    @Query("SELECT new edu.du.campusflow.dto.DiagEvaluationDetailDTO(" +
            "o.lectureId.curriculumSubject.subject.subjectId, " +
            "o.lectureId.semester.codeId, " +
            "o.lectureId.lectureName, " +
            "o.member.name) " +
            "FROM Ofregistration o " +
            "WHERE (:deptId IS NULL OR o.lectureId.curriculumSubject.subject.subjectId = :deptId) " +
            "AND o.lectureId.semester.codeId = :gradeCodeId " +
            "AND o.member.name = :studentName")
    List<DiagEvaluationDetailDTO> findEvaluationsByStudent(
            @Param("deptId") Long deptId,
            @Param("gradeCodeId") Long gradeCodeId,
            @Param("studentName") String studentName);

    @Query("SELECT new edu.du.campusflow.dto.DiagEvaluationDetailDTO(" +
            "o.lectureId.curriculumSubject.subject.subjectId, " +
            "o.lectureId.semester.codeId, " +
            "o.lectureId.lectureName, " +
            "o.member.name) " +
            "FROM Ofregistration o " +
            "WHERE (:deptId IS NULL OR o.lectureId.curriculumSubject.subject.subjectId = :deptId) " +
            "AND o.lectureId.semester.codeId = :gradeCodeId " +
            "AND o.lectureId.lectureName = :lectureName " +
            "AND o.member.name = :studentName")
    List<DiagEvaluationDetailDTO> findEvaluationsByLectureAndStudent(
            @Param("deptId") Long deptId,
            @Param("gradeCodeId") Long gradeCodeId,
            @Param("lectureName") String lectureName,
            @Param("studentName") String studentName);
}
