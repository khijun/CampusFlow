package edu.du.campusflow.repository;

import edu.du.campusflow.dto.AttendanceDTO;
import edu.du.campusflow.dto.ProfessorAttendanceDTO;
import edu.du.campusflow.entity.Attendance;
import edu.du.campusflow.entity.LectureTime;
import edu.du.campusflow.entity.Ofregistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    @Query("SELECT new edu.du.campusflow.dto.AttendanceDTO(" +
            "l.lectureName, " +
            "COALESCE(SUM(CASE WHEN a.attendanceStatus.codeId = 16 THEN 1 ELSE 0 END), 0), " +
            "COALESCE(SUM(CASE WHEN a.attendanceStatus.codeId = 17 THEN 1 ELSE 0 END), 0), " +
            "COALESCE(SUM(CASE WHEN a.attendanceStatus.codeId = 18 THEN 1 ELSE 0 END), 0), " +
            "COALESCE(MAX(CASE WHEN lw.week = 1 THEN a.attendanceStatus.codeId ELSE -1 END), -1), " +
            "COALESCE(MAX(CASE WHEN lw.week = 2 THEN a.attendanceStatus.codeId ELSE -1 END), -1), " +
            "COALESCE(MAX(CASE WHEN lw.week = 3 THEN a.attendanceStatus.codeId ELSE -1 END), -1), " +
            "COALESCE(MAX(CASE WHEN lw.week = 4 THEN a.attendanceStatus.codeId ELSE -1 END), -1), " +
            "COALESCE(MAX(CASE WHEN lw.week = 5 THEN a.attendanceStatus.codeId ELSE -1 END), -1), " +
            "COALESCE(MAX(CASE WHEN lw.week = 6 THEN a.attendanceStatus.codeId ELSE -1 END), -1), " +
            "COALESCE(MAX(CASE WHEN lw.week = 7 THEN a.attendanceStatus.codeId ELSE -1 END), -1), " +
            "COALESCE(MAX(CASE WHEN lw.week = 8 THEN a.attendanceStatus.codeId ELSE -1 END), -1), " +
            "COALESCE(MAX(CASE WHEN lw.week = 9 THEN a.attendanceStatus.codeId ELSE -1 END), -1), " +
            "COALESCE(MAX(CASE WHEN lw.week = 10 THEN a.attendanceStatus.codeId ELSE -1 END), -1), " +
            "COALESCE(MAX(CASE WHEN lw.week = 11 THEN a.attendanceStatus.codeId ELSE -1 END), -1), " +
            "COALESCE(MAX(CASE WHEN lw.week = 12 THEN a.attendanceStatus.codeId ELSE -1 END), -1), " +
            "COALESCE(MAX(CASE WHEN lw.week = 13 THEN a.attendanceStatus.codeId ELSE -1 END), -1), " +
            "COALESCE(MAX(CASE WHEN lw.week = 14 THEN a.attendanceStatus.codeId ELSE -1 END), -1), " +
            "COALESCE(MAX(CASE WHEN lw.week = 15 THEN a.attendanceStatus.codeId ELSE -1 END), -1) " +
            ") " +
            "FROM Attendance a " +
            "JOIN a.ofRegistration o " +
            "JOIN o.lectureId l " +
            "JOIN l.curriculumSubject cs " +
            "JOIN cs.curriculum c " +
            "JOIN a.lectureTime lt " +
            "JOIN lt.lectureWeek lw " +
            "WHERE o.member.memberId = :memberId " +
            "AND c.semester.codeId = :semester " +
            "AND c.curriculumYear = :year " +
            "GROUP BY l.lectureName")
    List<AttendanceDTO> findAttendanceByStudent(
            @Param("memberId") Long memberId,
            @Param("semester") Long semesterCodeId,
            @Param("year") Integer year
    );

    @Query("SELECT new edu.du.campusflow.dto.ProfessorAttendanceDTO(" +
            "s.memberId, COALESCE(s.name, ''), COALESCE(l.lectureName, ''), l.lectureId, " +
            "COALESCE(SUM(CASE WHEN a.attendanceStatus.codeId = 16 THEN 1 ELSE 0 END), 0), " +
            "COALESCE(SUM(CASE WHEN a.attendanceStatus.codeId = 17 THEN 1 ELSE 0 END), 0), " +
            "COALESCE(SUM(CASE WHEN a.attendanceStatus.codeId = 18 THEN 1 ELSE 0 END), 0), " +
            "COALESCE(MAX(CASE WHEN lw.week = 1 THEN a.attendanceStatus.codeId ELSE -1 END), -1), " +
            "COALESCE(MAX(CASE WHEN lw.week = 2 THEN a.attendanceStatus.codeId ELSE -1 END), -1), " +
            "COALESCE(MAX(CASE WHEN lw.week = 3 THEN a.attendanceStatus.codeId ELSE -1 END), -1), " +
            "COALESCE(MAX(CASE WHEN lw.week = 4 THEN a.attendanceStatus.codeId ELSE -1 END), -1), " +
            "COALESCE(MAX(CASE WHEN lw.week = 5 THEN a.attendanceStatus.codeId ELSE -1 END), -1), " +
            "COALESCE(MAX(CASE WHEN lw.week = 6 THEN a.attendanceStatus.codeId ELSE -1 END), -1), " +
            "COALESCE(MAX(CASE WHEN lw.week = 7 THEN a.attendanceStatus.codeId ELSE -1 END), -1), " +
            "COALESCE(MAX(CASE WHEN lw.week = 8 THEN a.attendanceStatus.codeId ELSE -1 END), -1), " +
            "COALESCE(MAX(CASE WHEN lw.week = 9 THEN a.attendanceStatus.codeId ELSE -1 END), -1), " +
            "COALESCE(MAX(CASE WHEN lw.week = 10 THEN a.attendanceStatus.codeId ELSE -1 END), -1), " +
            "COALESCE(MAX(CASE WHEN lw.week = 11 THEN a.attendanceStatus.codeId ELSE -1 END), -1), " +
            "COALESCE(MAX(CASE WHEN lw.week = 12 THEN a.attendanceStatus.codeId ELSE -1 END), -1), " +
            "COALESCE(MAX(CASE WHEN lw.week = 13 THEN a.attendanceStatus.codeId ELSE -1 END), -1), " +
            "COALESCE(MAX(CASE WHEN lw.week = 14 THEN a.attendanceStatus.codeId ELSE -1 END), -1), " +
            "COALESCE(MAX(CASE WHEN lw.week = 15 THEN a.attendanceStatus.codeId ELSE -1 END), -1) " +
            ") " +
            "FROM Attendance a " +
            "JOIN a.ofRegistration o " +
            "JOIN o.member s " +
            "JOIN o.lectureId l " +
            "JOIN l.curriculumSubject cs " +
            "JOIN cs.curriculum c " +
            "JOIN a.lectureTime lt " +
            "JOIN lt.lectureWeek lw " +
            "WHERE l.lectureId = :lectureId " +
            "AND c.semester.codeId = :semester " +
            "AND c.curriculumYear = :year " +
            "GROUP BY s.memberId, s.name, l.lectureName, l.lectureId")
    List<ProfessorAttendanceDTO> findAttendanceByProfessor(
            @Param("lectureId") Long lectureId,
            @Param("semester") Long semesterCodeId,
            @Param("year") Integer year
    );

    @Modifying
    @Transactional
    @Query(value = "UPDATE attendance a " +
            "JOIN ofregistration o ON a.ofregistration_id = o.id " +
            "JOIN lecture_time lt ON a.lecture_time_id = lt.lecture_time_id " +
            "JOIN lecture_week lw ON lt.lecture_week_id = lw.lecture_week_id " +
            "SET a.status = :statusCode, a.updated_at = NOW() " +
            "WHERE o.member_id = :studentId " +
            "AND o.lecture_id = :lectureId " +
            "AND lw.week = :week", nativeQuery = true)
    int updateAttendanceStatus(@Param("studentId") Long studentId,
                               @Param("lectureId") Long lectureId,
                               @Param("week") Integer week,
                               @Param("statusCode") Long statusCode);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO attendance (ofregistration_id, lecture_time_id, status, created_at, updated_at) " +
            "SELECT o.id, lt.lecture_time_id, :statusCode, NOW(), NOW() " +
            "FROM ofregistration o " +
            "JOIN lecture_week lw ON lw.lecture_id = o.lecture_id " +
            "JOIN lecture_time lt ON lt.lecture_week_id = lw.lecture_week_id " +
            "WHERE o.member_id = :studentId " +
            "AND o.lecture_id = :lectureId " +
            "AND lw.week = :week " +
            "AND NOT EXISTS (SELECT 1 FROM attendance a WHERE a.ofregistration_id = o.id AND a.lecture_time_id = lt.lecture_time_id)", nativeQuery = true)
    void insertAttendance(@Param("studentId") Long studentId,
                          @Param("lectureId") Long lectureId,
                          @Param("week") Integer week,
                          @Param("statusCode") Long statusCode);

    // 특정 학생의 특정 강의 시간에 대한 출석 데이터 존재 여부 확인
    boolean existsByOfRegistrationAndLectureTime(Ofregistration ofRegistration, LectureTime lectureTime);

}
