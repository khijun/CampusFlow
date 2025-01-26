package edu.du.campusflow.repository;

import edu.du.campusflow.dto.AttendanceDTO;
import edu.du.campusflow.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

}
