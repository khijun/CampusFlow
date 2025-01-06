package edu.du.campusflow.service;

import edu.du.campusflow.entity.AcademicCalendar;
import edu.du.campusflow.repository.AcademicCalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcademicCalendarService {

    @Autowired
    private AcademicCalendarRepository academicCalendarRepository;

    // 모든 학사 일정 조회
    public List<AcademicCalendar> getAllAcademicCalendars() {
        return academicCalendarRepository.findAll(); // 모든 학사 일정 조회
    }

    // 학사 일정 추가
    public AcademicCalendar createAcademicCalendar(AcademicCalendar academicCalendar) {
        return academicCalendarRepository.save(academicCalendar); // 학사 일정 저장
    }

    public ResponseEntity<Void> addAcademicCalendar(AcademicCalendar academicCalendar) {
        try {
            academicCalendarRepository.save(academicCalendar);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            // 예외 처리 로직 추가
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    public List<AcademicCalendar> getAllCalendars() {
        return academicCalendarRepository.findAll(); // 모든 학사 일정 반환
    }
}
