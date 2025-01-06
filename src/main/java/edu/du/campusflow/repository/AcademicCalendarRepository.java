package edu.du.campusflow.repository;

import edu.du.campusflow.entity.AcademicCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;

public interface AcademicCalendarRepository extends JpaRepository<AcademicCalendar, Long> {
    
    @Query("SELECT COUNT(a) > 0 FROM AcademicCalendar a " +
           "WHERE a.title = :title " +
           "AND ((a.startDate BETWEEN :startDate AND :endDate) " +
           "OR (a.endDate BETWEEN :startDate AND :endDate))")
    boolean existsByTitleAndDateRange(
        @Param("title") String title,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
}
