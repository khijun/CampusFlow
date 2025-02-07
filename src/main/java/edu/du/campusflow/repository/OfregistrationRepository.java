package edu.du.campusflow.repository;

import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.Lecture;
import edu.du.campusflow.entity.Member;
import edu.du.campusflow.entity.Ofregistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
            "AND o.reg_status = 86 " +
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
            "AND o.reg_status = 86 " +
            "GROUP BY o.id, l.lecture_name, m.name, cc.code_value",
            nativeQuery = true)
    List<Map<String, Object>> findLecEvalLecturesWithEvalStatus(@Param("studentId") Long studentId);

    @Query("SELECT o FROM Ofregistration o WHERE o.member.memberId = :memberId AND o.lectureId.lectureId = :lectureId")
    List<Ofregistration> findByMember_MemberIdAndLectureId(@Param("memberId") Long memberId, @Param("lectureId") Long lectureId);
    @Query("SELECT o FROM Ofregistration o WHERE o.lectureId.lectureId = :lectureId")
    List<Ofregistration> findByLectureId(Long lectureId);

    // 중복 체크를 위한 메서드
    boolean existsByMemberAndLectureId(Member member, Lecture lecture);

    // 학생의 모든 수강신청 내역 조회
    List<Ofregistration> findByMember(Member member);

    // LECTURE_PENDING 상태인 강의의 수강신청 목록 조회
    @Query("SELECT o FROM Ofregistration o WHERE o.lectureId.lectureStatus.codeValue = :status")
    List<Ofregistration> findByLectureStatus(@Param("status") String status);

    // 특정 강의와 학생의 수강신청 정보 조회
    @Query("SELECT o FROM Ofregistration o WHERE o.lectureId.lectureId = :lectureId AND o.member.memberId = :memberId")
    Optional<Ofregistration> findByLectureIdAndMemberId(@Param("lectureId") Long lectureId, @Param("memberId") Long memberId);

    // 과제 제출 페이지에서 학생의 수강중인 강의 조회
    List<Ofregistration> findByMemberAndLectureId_Semester(Member student, CommonCode semester);

    // 수강신청 취소를 위한 메서드
    @Modifying
    @Query("DELETE FROM Ofregistration o WHERE o.lectureId.lectureId = :lectureId AND o.member.memberId = :memberId")
    void deleteByLectureIdAndMemberMemberId(@Param("lectureId") Long lectureId, @Param("memberId") Long memberId);

    Optional<Ofregistration> findByLectureIdAndMember_MemberId(Long lectureId, Long memberId);

    @Query(value = "SELECT " +
            "l.lecture_id as lectureId, " +
            "l.lecture_name as lectureName, " +
            "m.member_id as professorId, " +
            "m.name as professorName, " +
            "d.dept_name as deptName, " +
            "st.code_name as subjectType, " +
            "g.code_name as grade, " +
            "s.subject_credits as subjectCredits, " +
            "MAX(ld.code_name) as lectureDay, " +
            "MAX(st1.code_name) as startTime, " +
            "MAX(et.code_name) as endTime, " +
            "MAX(f.facility_name) as facilityName, " +
            "l.max_students as maxStudents, " +
            "l.current_students as currentStudents, " +
            "dn.code_name as dayNight, " +
            "COALESCE(MAX(rs.code_value), 'NOT_REQUESTED') as regStatus " +
            "FROM lecture l " +
            "JOIN member m ON l.member_id = m.member_id " +
            "JOIN curriculum_subject cs ON l.curriculum_subject_id = cs.curriculum_subject_id " +
            "JOIN curriculum c ON cs.curriculum_id = c.curriculum_id " +
            "JOIN dept d ON c.dept_id = d.dept_id " +
            "JOIN subject s ON cs.subject_id = s.subject_id " +
            "JOIN common_code st ON cs.subject_type = st.code_id " +
            "JOIN common_code g ON c.grade = g.code_id " +
            "JOIN common_code dn ON c.day_night = dn.code_id " +
            "LEFT JOIN lecture_week lw ON l.lecture_id = lw.lecture_id " +
            "LEFT JOIN lecture_time lt ON lw.lecture_week_id = lt.lecture_week_id " +
            "LEFT JOIN common_code ld ON lt.lecture_day = ld.code_id " +
            "LEFT JOIN common_code st1 ON lt.start_time = st1.code_id " +
            "LEFT JOIN common_code et ON lt.end_time = et.code_id " +
            "LEFT JOIN facility f ON lt.facility_id = f.facility_id " +
            "LEFT JOIN ofregistration o ON l.lecture_id = o.lecture_id AND o.member_id = :studentId " +
            "LEFT JOIN common_code rs ON o.reg_status = rs.code_id " +
            "WHERE l.lecture_status = (" +
            "    SELECT code_id FROM common_code WHERE code_value = 'LECTURE_PENDING'" +
            ") " +
            "GROUP BY l.lecture_id, l.lecture_name, m.member_id, m.name, d.dept_name, " +
            "st.code_name, g.code_name, s.subject_credits, l.max_students, l.current_students, dn.code_name", 
            nativeQuery = true)
    List<Map<String, Object>> findAllAvailableLecturesForStudent(@Param("studentId") Long studentId);

    @Query(value = "SELECT " +
            "o.id as registrationId, " +
            "l.lecture_id as lectureId, " +
            "l.lecture_name as lectureName, " +
            "m.member_id as professorId, " +
            "m.name as professorName, " +
            "d.dept_name as deptName, " +
            "st.code_name as subjectType, " +
            "g.code_name as grade, " +
            "s.subject_credits as subjectCredits, " +
            "MAX(ld.code_name) as lectureDay, " +
            "MAX(st1.code_name) as startTime, " +
            "MAX(et.code_name) as endTime, " +
            "MAX(f.facility_name) as facilityName, " +
            "l.max_students as maxStudents, " +
            "l.current_students as currentStudents, " +
            "dn.code_name as dayNight, " +
            "rs.code_name as regStatus " +
            "FROM ofregistration o " +
            "JOIN lecture l ON o.lecture_id = l.lecture_id " +
            "JOIN member m ON l.member_id = m.member_id " +
            "JOIN curriculum_subject cs ON l.curriculum_subject_id = cs.curriculum_subject_id " +
            "JOIN curriculum c ON cs.curriculum_id = c.curriculum_id " +
            "JOIN dept d ON c.dept_id = d.dept_id " +
            "JOIN subject s ON cs.subject_id = s.subject_id " +
            "JOIN common_code st ON cs.subject_type = st.code_id " +
            "JOIN common_code g ON c.grade = g.code_id " +
            "JOIN common_code dn ON c.day_night = dn.code_id " +
            "JOIN common_code rs ON o.reg_status = rs.code_id " +
            "LEFT JOIN lecture_week lw ON l.lecture_id = lw.lecture_id " +
            "LEFT JOIN lecture_time lt ON lw.lecture_week_id = lt.lecture_week_id " +
            "LEFT JOIN common_code ld ON lt.lecture_day = ld.code_id " +
            "LEFT JOIN common_code st1 ON lt.start_time = st1.code_id " +
            "LEFT JOIN common_code et ON lt.end_time = et.code_id " +
            "LEFT JOIN facility f ON lt.facility_id = f.facility_id " +
            "WHERE o.member_id = :studentId " +
            "GROUP BY o.id, l.lecture_id, l.lecture_name, m.member_id, m.name, d.dept_name, " +
            "st.code_name, g.code_name, s.subject_credits, l.max_students, l.current_students, " +
            "dn.code_name, rs.code_name", 
            nativeQuery = true)
    List<Map<String, Object>> findStudentRegistrationsOptimized(@Param("studentId") Long studentId);

    @Query(value = "SELECT " +
            "o.id as registrationId, " +
            "l.lecture_id as lectureId, " +
            "l.lecture_name as lectureName, " +
            "m.member_id as studentId, " +
            "m.name as studentName, " +
            "d.dept_name as deptName, " +
            "st.code_name as subjectType, " +
            "g.code_name as grade, " +
            "s.subject_credits as subjectCredits, " +
            "MAX(ld.code_name) as lectureDay, " +
            "MAX(st1.code_name) as startTime, " +
            "MAX(et.code_name) as endTime, " +
            "MAX(f.facility_name) as facilityName, " +
            "l.max_students as maxStudents, " +
            "l.current_students as currentStudents, " +
            "dn.code_name as dayNight, " +
            "rs.code_name as regStatus " +
            "FROM ofregistration o " +
            "JOIN lecture l ON o.lecture_id = l.lecture_id " +
            "JOIN member m ON o.member_id = m.member_id " +
            "JOIN curriculum_subject cs ON l.curriculum_subject_id = cs.curriculum_subject_id " +
            "JOIN curriculum c ON cs.curriculum_id = c.curriculum_id " +
            "JOIN dept d ON c.dept_id = d.dept_id " +
            "JOIN subject s ON cs.subject_id = s.subject_id " +
            "JOIN common_code st ON cs.subject_type = st.code_id " +
            "JOIN common_code g ON c.grade = g.code_id " +
            "JOIN common_code dn ON c.day_night = dn.code_id " +
            "JOIN common_code rs ON o.reg_status = rs.code_id " +
            "LEFT JOIN lecture_week lw ON l.lecture_id = lw.lecture_id " +
            "LEFT JOIN lecture_time lt ON lw.lecture_week_id = lt.lecture_week_id " +
            "LEFT JOIN common_code ld ON lt.lecture_day = ld.code_id " +
            "LEFT JOIN common_code st1 ON lt.start_time = st1.code_id " +
            "LEFT JOIN common_code et ON lt.end_time = et.code_id " +
            "LEFT JOIN facility f ON lt.facility_id = f.facility_id " +
            "WHERE l.lecture_status = (" +
            "    SELECT code_id FROM common_code WHERE code_value = 'LECTURE_PENDING'" +
            ") " +
            "GROUP BY o.id, l.lecture_id, l.lecture_name, m.member_id, m.name, d.dept_name, " +
            "st.code_name, g.code_name, s.subject_credits, l.max_students, l.current_students, " +
            "dn.code_name, rs.code_name", 
            nativeQuery = true)
    List<Map<String, Object>> findAllPendingRegistrationsForAdmin();
}