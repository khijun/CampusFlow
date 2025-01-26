package edu.du.campusflow.service;

import edu.du.campusflow.dto.AttendanceDTO;
import edu.du.campusflow.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final AuthService authService;

    public List<AttendanceDTO> getStudentAttendance(Long semesterCodeId, Integer year) {
        Long memberId = authService.getCurrentMemberId();
        if (memberId == null) {
            throw new IllegalStateException("현재 로그인한 사용자가 없습니다.");
        }
        return attendanceRepository.findAttendanceByStudent(memberId, semesterCodeId, year);
    }
}
