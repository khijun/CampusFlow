package edu.du.campusflow.service;

import edu.du.campusflow.dto.AttendanceDTO;
import edu.du.campusflow.dto.LectureListDTO;
import edu.du.campusflow.dto.ProfessorAttendanceDTO;
import edu.du.campusflow.entity.Lecture;
import edu.du.campusflow.repository.AttendanceRepository;
import edu.du.campusflow.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

        if (lectures.isEmpty()) {
            System.out.println("❌ 강의 목록이 비어 있습니다.");
        }

        return lectures.stream()
                .map(lecture -> new LectureListDTO(lecture.getLectureId(), lecture.getLectureName()))
                .collect(Collectors.toList());
    }

    public List<ProfessorAttendanceDTO> getProfessorAttendance(Integer year, Long semesterCodeId, Long lectureId) {
        return attendanceRepository.findAttendanceByProfessor(lectureId, semesterCodeId, year);
    }

    @Transactional
    public void updateAttendance(Long lectureId, List<ProfessorAttendanceDTO> attendanceData) {
        if (lectureId == null || attendanceData == null || attendanceData.isEmpty()) {
            throw new IllegalArgumentException("❌ 강의 ID 또는 출결 데이터가 유효하지 않습니다.");
        }

        for (ProfessorAttendanceDTO dto : attendanceData) {
            for (int week = 1; week <= 15; week++) {
                String status = getWeekStatus(dto, week);
                Long statusCode = convertStatusCode(status);

                if (dto.getStudentId() != null && statusCode != null) {
                    int updatedRows = attendanceRepository.updateAttendanceStatus(
                            dto.getStudentId(), lectureId, week, statusCode);

                    if (updatedRows == 0) {
                        System.out.println("⚠️ 출결 업데이트 실패: studentId=" + dto.getStudentId() + ", lectureId=" + lectureId + ", week=" + week + ", statusCode=" + statusCode);
                    } else {
                        System.out.println("✅ 출결 업데이트 성공: studentId=" + dto.getStudentId() + ", lectureId=" + lectureId + ", week=" + week + ", statusCode=" + statusCode + " (변경된 행: " + updatedRows + ")");
                    }
                }
            }
        }
    }

    private Long convertStatusCode(String status) {
        if (status == null || status.equals("-")) return null;
        switch (status) {
            case "P": return 16L;
            case "L": return 17L;
            case "A": return 18L;
            default: return null;
        }
    }

    private String getWeekStatus(ProfessorAttendanceDTO dto, int week) {
        switch (week) {
            case 1: return dto.getWeek1();
            case 2: return dto.getWeek2();
            case 3: return dto.getWeek3();
            case 4: return dto.getWeek4();
            case 5: return dto.getWeek5();
            case 6: return dto.getWeek6();
            case 7: return dto.getWeek7();
            case 8: return dto.getWeek8();
            case 9: return dto.getWeek9();
            case 10: return dto.getWeek10();
            case 11: return dto.getWeek11();
            case 12: return dto.getWeek12();
            case 13: return dto.getWeek13();
            case 14: return dto.getWeek14();
            case 15: return dto.getWeek15();
            default: return "-";
        }
    }

    public Long findLectureIdByStudent(Long studentId) {
        return attendanceRepository.findLectureIdByStudent(studentId);
    }
}
