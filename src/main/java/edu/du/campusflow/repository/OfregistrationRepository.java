package edu.du.campusflow.repository;

import edu.du.campusflow.entity.Lecture;
import edu.du.campusflow.entity.Member;
import edu.du.campusflow.entity.Ofregistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface OfregistrationRepository extends JpaRepository<Ofregistration, Long> {

    List<Ofregistration> findByLectureId_LectureId(Long lectureId);

    // diag 관련 검색 쿼리 (관리자/교수)
    @Query(value = "SELECT " +
            "l.lecture_name AS lectureName, " +
            "dq.question_name AS questionName, " +
            "di.score AS score " +
            "FROM ofregistration o " +
            "INNER JOIN lecture l ON o.lecture_id = l.lecture_id " +
            "INNER JOIN curriculum_subject cs ON l.curriculum_subject_id = cs.curriculum_subject_id " +
            "INNER JOIN curriculum c ON cs.curriculum_id = c.curriculum_id " +
            "INNER JOIN dept d ON c.dept_id = d.dept_id " +
            "INNER JOIN common_code sem ON l.semester = sem.code_id " +
            "INNER JOIN member m ON o.member_id = m.member_id " +  // member 명시적 조인
            "INNER JOIN diag_items di ON di.ofregistration_id = o.id " +
            "INNER JOIN diag_questions dq ON di.question_id = dq.question_id " +
            "WHERE (:deptId IS NULL OR d.dept_id = :deptId) " +
            "AND (:grade IS NULL OR sem.code_value = :grade) " +
            "AND (:lectureName IS NULL OR l.lecture_name LIKE CONCAT('%', :lectureName, '%')) " +
            "AND (:studentName IS NULL OR m.name LIKE CONCAT('%', :studentName, '%')) " +
            "ORDER BY l.lecture_name, dq.question_name",
            nativeQuery = true)
    List<Object[]> findEvaluationsBySearchCriteria(
            @Param("deptId") Long deptId,
            @Param("grade") String grade,
            @Param("lectureName") String lectureName,
            @Param("studentName") String studentName
    );

    // diag 학생 진단 평가 여부 (Y/N)
    @Query(value = "SELECT " +
            "o.id as ofregistrationId, " +
            "l.lecture_name as lectureName, " +
            "m.name as professorName, " +
            "cc.code_value as semester, " +
            "CASE WHEN COUNT(di.answer_id) > 0 THEN 'Y' ELSE 'N' END as evalStatus " +
            "FROM ofregistration o " +
            "INNER JOIN lecture l ON o.lecture_id = l.lecture_id " +
            "INNER JOIN member m ON l.member_id = m.member_id " +
            "INNER JOIN common_code cc ON l.semester = cc.code_id " +
            "LEFT OUTER JOIN diag_items di ON di.ofregistration_id = o.id " +
            "WHERE o.member_id = :studentId " +
            "GROUP BY o.id, l.lecture_name, m.name, cc.code_value",
            nativeQuery = true)
    List<Map<String, Object>> findDiagLecturesWithEvalStatus(@Param("studentId") Long studentId);

    // lec 관련 검색 쿼리 (관리자/교수용)
    @Query(value = "SELECT " +
            "l.lecture_name AS lectureName, " +
            "lq.question_name AS questionName, " +
            "li.score AS score " +
            "FROM ofregistration o " +
            "INNER JOIN lecture l ON o.lecture_id = l.lecture_id " +
            "INNER JOIN curriculum_subject cs ON l.curriculum_subject_id = cs.curriculum_subject_id " +
            "INNER JOIN curriculum c ON cs.curriculum_id = c.curriculum_id " +
            "INNER JOIN dept d ON c.dept_id = d.dept_id " +
            "INNER JOIN common_code sem ON l.semester = sem.code_id " +
            "INNER JOIN member m ON o.member_id = m.member_id " +
            "INNER JOIN lec_items li ON li.ofregistration_id = o.id " +
            "INNER JOIN lec_questions lq ON li.question_id = lq.question_id " +
            "WHERE (:deptId IS NULL OR d.dept_id = :deptId) " +
            "AND (:grade IS NULL OR sem.code_value = :grade) " +
            "AND (:lectureName IS NULL OR l.lecture_name LIKE CONCAT('%', :lectureName, '%')) " +
            "AND (:studentName IS NULL OR m.name LIKE CONCAT('%', :studentName, '%')) " +
            "ORDER BY l.lecture_name, lq.question_name",
            nativeQuery = true)
    List<Object[]> findLecEvaluationsBySearchCriteria(
            @Param("deptId") Long deptId,
            @Param("grade") String grade,
            @Param("lectureName") String lectureName,
            @Param("studentName") String studentName
    );

    // lec 학생 강의 평가 여부 (Y/N)
    @Query(value = "SELECT " +
            "o.id as ofregistrationId, " +
            "l.lecture_name as lectureName, " +
            "m.name as professorName, " +
            "cc.code_value as semester, " +
            "CASE WHEN COUNT(li.answer_id) > 0 THEN 'Y' ELSE 'N' END as evalStatus " +
            "FROM ofregistration o " +
            "INNER JOIN lecture l ON o.lecture_id = l.lecture_id " +
            "INNER JOIN member m ON l.member_id = m.member_id " +
            "INNER JOIN common_code cc ON l.semester = cc.code_id " +
            "LEFT OUTER JOIN lec_items li ON li.ofregistration_id = o.id " +
            "WHERE o.member_id = :studentId " +
            "GROUP BY o.id, l.lecture_name, m.name, cc.code_value",
            nativeQuery = true)
    List<Map<String, Object>> findLecEvalLecturesWithEvalStatus(@Param("studentId") Long studentId);

    @Query("SELECT o FROM Ofregistration o WHERE o.member.memberId = :memberId AND o.lectureId.lectureId = :lectureId")
    List<Ofregistration> findByMember_MemberIdAndLectureId(@Param("memberId") Long memberId, @Param("lectureId") Long lectureId);
    @Query("SELECT o FROM Ofregistration o WHERE o.lectureId.lectureId = :lectureId")
    List<Ofregistration> findByLectureId(Long lectureId);

    // 중복 체크를 위한 메서드 추가
    boolean existsByMemberAndLectureId(Member member, Lecture lecture);

    // 학생의 모든 수강신청 내역 조회
    List<Ofregistration> findByMember(Member member);

    // LECTURE_PENDING 상태인 강의의 수강신청 목록 조회
    @Query("SELECT o FROM Ofregistration o WHERE o.lectureId.lectureStatus.codeValue = :status")
    List<Ofregistration> findByLectureStatus(@Param("status") String status);

    // 특정 강의와 학생의 수강신청 정보 조회
    @Query("SELECT o FROM Ofregistration o WHERE o.lectureId.lectureId = :lectureId AND o.member.memberId = :memberId")
    Optional<Ofregistration> findByLectureIdAndMemberId(@Param("lectureId") Long lectureId, @Param("memberId") Long memberId);
}