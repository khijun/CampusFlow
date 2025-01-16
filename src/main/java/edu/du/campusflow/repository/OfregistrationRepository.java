package edu.du.campusflow.repository;

import edu.du.campusflow.entity.Ofregistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface OfregistrationRepository extends JpaRepository<Ofregistration, Long> {

    List<Ofregistration> findByLectureId_LectureId(Long lectureId);

    @Query("SELECT o FROM Ofregistration o WHERE o.member.memberId = :memberId AND o.lectureId.lectureId = :lectureId")
    List<Ofregistration> findByMember_MemberIdAndLectureId(@Param("memberId") Long memberId, @Param("lectureId") Long lectureId);

    @Query("SELECT o FROM Ofregistration o WHERE o.lectureId.lectureId = :lectureId")
    List<Ofregistration> findByLectureId(Long lectureId);

    // 진단 평가 통합 쿼리
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


    // 강의평가 목록 조회 쿼리
    @Query(value = "SELECT " +
            "o.id as ofregistrationId, " +
            "l.lecture_name as lectureName, " +
            "m.name as professorName, " +  // 교수 이름
            "cc.code_value as semester, " +  // 학기 정보
            "CASE WHEN COUNT(li.answer_id) > 0 THEN 'Y' ELSE 'N' END as evalStatus " +
            "FROM ofregistration o " +
            "INNER JOIN lecture l ON o.lecture_id = l.lecture_id " +
            "INNER JOIN member m ON l.member_id = m.member_id " +  // 교수 정보
            "INNER JOIN common_code cc ON l.semester = cc.code_id " +  // 학기 정보
            "LEFT OUTER JOIN lec_items li ON li.ofregistration_id = o.id " +
            "WHERE o.member_id = :studentId " +  // 학생 ID만으로 조회
            "GROUP BY o.id, l.lecture_name, m.name, cc.code_value ",
            nativeQuery = true)
    List<Map<String, Object>> findLecEvalLecturesWithEvalStatus(@Param("studentId") Long studentId);
}