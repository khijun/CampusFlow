package edu.du.campusflow.service;

import edu.du.campusflow.entity.AcademicCalendar;
import edu.du.campusflow.repository.AcademicCalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AcademicCalendarService {

    @Autowired
    private AcademicCalendarRepository academicCalendarRepository;

    // 모든 학사 일정 조회
    public List<AcademicCalendar> getAllAcademicCalendars() {
        return academicCalendarRepository.findAll(); // 모든 학사 일정 조회
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

    @Transactional(readOnly = true)
    public List<AcademicCalendar> getAllCalendars() {
        return academicCalendarRepository.findAll(); // 모든 학사 일정 반환
    }

    // 일정 삭제
    public void deleteCalendar(Long id) {
        academicCalendarRepository.deleteById(id);
    }

    @Transactional
    public AcademicCalendar createAcademicCalendar(AcademicCalendar calendar) {
        // 날짜 유효성 검사
        if (calendar.getStartDate() == null || calendar.getEndDate() == null) {
            throw new IllegalArgumentException("시작일과 종료일은 필수입니다.");
        }
        
        // 같은 기간에 동일한 제목의 일정이 있는지 확인
        boolean exists = academicCalendarRepository.existsByTitleAndDateRange(
            calendar.getTitle(),
            calendar.getStartDate(),
            calendar.getEndDate()
        );
        
        if (exists) {
            throw new IllegalStateException("해당 기간에 동일한 제목의 일정이 이미 존재합니다.");
        }
        
        return academicCalendarRepository.save(calendar);
    }
}
