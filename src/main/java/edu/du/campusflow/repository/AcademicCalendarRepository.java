package edu.du.campusflow.repository;

import edu.du.campusflow.entity.AcademicCalendar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcademicCalendarRepository extends JpaRepository<AcademicCalendar, Long> {
}
