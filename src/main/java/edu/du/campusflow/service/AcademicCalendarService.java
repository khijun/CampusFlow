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

    @Autowired
    private AuthService authService;

    // 모든 학사 일정 조회
    public List<AcademicCalendar> getAllAcademicCalendars() {
        return academicCalendarRepository.findAll();
    }

    // 학사 일정 추가
    public ResponseEntity<Void> addAcademicCalendar(AcademicCalendar academicCalendar) {
        try {
            var currentMember = authService.getCurrentMember();
            if (currentMember == null || !"ADMIN".equals(currentMember.getMemberType().getCodeValue())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            
            academicCalendar.setMember(currentMember);
            academicCalendarRepository.save(academicCalendar);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<?> updateAcademicCalendar(AcademicCalendar academicCalendar) {
        try {
            var currentMember = authService.getCurrentMember();
            if (currentMember == null || !"ADMIN".equals(currentMember.getMemberType().getCodeValue())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // 기존 일정이 있는지 확인
            AcademicCalendar existingCalendar = academicCalendarRepository.findById(academicCalendar.getCalendarId())
                    .orElseThrow(() -> new RuntimeException("해당 학사 일정을 찾을 수 없습니다."));

            // 업데이트할 내용 설정
            existingCalendar.setTitle(academicCalendar.getTitle());
            existingCalendar.setStartDate(academicCalendar.getStartDate());
            existingCalendar.setEndDate(academicCalendar.getEndDate());
            existingCalendar.setDescription(academicCalendar.getDescription());
            existingCalendar.setMember(currentMember);

            // 저장
            academicCalendarRepository.save(existingCalendar);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("일정 수정 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public ResponseEntity<?> deleteCalendar(Long id) {
        try {
            var currentMember = authService.getCurrentMember();
            if (currentMember == null || !"ADMIN".equals(currentMember.getMemberType().getCodeValue())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // 삭제할 일정이 있는지 확인
            AcademicCalendar calendar = academicCalendarRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("해당 학사 일정을 찾을 수 없습니다."));

            // 일정 삭제
            academicCalendarRepository.delete(calendar);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("일정 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public List<AcademicCalendar> getAllCalendars() {
        return academicCalendarRepository.findAll();
    }
}
