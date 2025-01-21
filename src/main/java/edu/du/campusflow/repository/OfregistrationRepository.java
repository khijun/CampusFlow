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

    // lec 학생 강의 평가 진단 여부 (Y/N)
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