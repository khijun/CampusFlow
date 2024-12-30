package edu.du.campusflow.service;

import edu.du.campusflow.entity.AcademicCalendar;
import edu.du.campusflow.repository.AcademicCalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcademicCalendarService {

        @Autowired
        private AcademicCalendarRepository academicCalendarRepository;

        public AcademicCalendar createAcademicCalendar(AcademicCalendar Calendar) {
            return academicCalendarRepository.save(Calendar);
        }

        public List<AcademicCalendar> getAllAcademicCalendars() {
            return academicCalendarRepository.findAll();
        }
}
