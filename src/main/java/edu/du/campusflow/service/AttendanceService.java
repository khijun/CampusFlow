package edu.du.campusflow.service;

import edu.du.campusflow.dto.AttendanceDTO;
import edu.du.campusflow.dto.LectureListDTO;
import edu.du.campusflow.dto.ProfessorAttendanceDTO;
import edu.du.campusflow.entity.Lecture;
import edu.du.campusflow.repository.AttendanceRepository;
import edu.du.campusflow.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final LectureRepository lectureRepository;
    private final AuthService authService;

    public List<AttendanceDTO> getStudentAttendance(Long semesterCodeId, Integer year) {
        Long memberId = authService.getCurrentMemberId();
        if (memberId == null) {
            throw new IllegalStateException("현재 로그인한 사용자가 없습니다.");
        }
        return attendanceRepository.findAttendanceByStudent(memberId, semesterCodeId, year);
    }

    public List<LectureListDTO> getProfessorLectures() {
        Long professorId = authService.getCurrentMemberId();
        if (professorId == null) {
            throw new IllegalStateException("현재 로그인한 사용자가 없습니다.");
        }

        List<Lecture> lectures = lectureRepository.findByMember_MemberId(professorId);

        return lectures.stream()
                .map(lecture -> new LectureListDTO(lecture.getLectureId(), lecture.getLectureName()))
                .collect(Collectors.toList());
    }

    public List<ProfessorAttendanceDTO> getProfessorAttendance(Integer year, Long semesterCodeId, Long lectureId) {
        return attendanceRepository.findAttendanceByProfessor(lectureId, semesterCodeId, year);
    }
}