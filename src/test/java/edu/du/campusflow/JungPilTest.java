package edu.du.campusflow;

import edu.du.campusflow.dto.LectureDTO;
import edu.du.campusflow.entity.*;
import edu.du.campusflow.repository.*;
import edu.du.campusflow.service.LectureService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class JungPilTest {

    @Autowired
    private LectureService lectureService;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private CommonCodeRepository commonCodeRepository;

    @Autowired
    private CurriculumSubjectRepository curriculumSubjectRepository;


    //강의 개설 테스트
    @Test
    public void createLectureTest() {
        // given
        LectureDTO lectureDTO = new LectureDTO();
        lectureDTO.setLectureName("테스트 강의");
        lectureDTO.setMaxStudents(30);
        lectureDTO.setCurriculumSubjectId(1L); // 실제 DB에 존재하는 ID로 변경 필요

        // when
        assertDoesNotThrow(() -> {
            lectureService.createLecture(lectureDTO);
        });

        // then
//        Lecture savedLecture = lectureRepository.findByLectureName("테스트 강의")
//                .orElse(null);
//        assertNotNull(savedLecture);
//        assertEquals(30, savedLecture.getMaxStudents());
    }

    //승인 강의 목록 조회 테스트
    @Test
    public void getApprovedLecturesTest() {
        // given
        String semesterCode = "SECOND_SEMESTER";
        String professorId = "1001"; // 실제 DB에 존재하는 교수 ID로 변경 필요

        // when
        List<LectureDTO> approvedLectures = lectureService.getApprovedLectures(semesterCode, professorId);

        // then
        assertNotNull(approvedLectures);
        approvedLectures.forEach(lecture -> {
            assertNotNull(lecture.getLectureName());
            assertNotNull(lecture.getSemesterName());
            assertTrue(lecture.getMaxStudents() > 0);
        });
    }

    //승인 대기 중인 강의 목록 조회 테스트
    @Test
    public void getPendingLecturesTest() {
        // given
        String semesterCode = "SECOND_SEMESTER";
        String professorId = "1001"; // 실제 DB에 존재하는 교수 ID로 변경 필요

        // when
        List<LectureDTO> pendingLectures = lectureService.getPendingLectures(semesterCode, professorId);

        // then
        assertNotNull(pendingLectures);
        pendingLectures.forEach(lecture -> {
            assertNotNull(lecture.getLectureName());
            assertNotNull(lecture.getLectureStatus());
            assertEquals("승인 대기", lecture.getLectureStatus());
        });
    }

    //강의 승인 테스트
    @Test
    public void approveLectureTest() {
        // given
        LectureDTO lectureDTO = new LectureDTO();
        lectureDTO.setLectureId(1L); // 실제 DB에 존재하는 강의 ID로 변경 필요
        lectureDTO.setFacilityId(1L); // 실제 DB에 존재하는 강의실 ID로 변경 필요
        lectureDTO.setLectureDays("MON");
        lectureDTO.setWeek(15);
        lectureDTO.setStartTime("PERIOD_FIRST");
        lectureDTO.setEndTime("PERIOD_SECOND");

        // when
        assertDoesNotThrow(() -> {
            lectureService.approveLecture(lectureDTO);
        });

        // then
        Lecture approvedLecture = lectureRepository.findById(1L)
                .orElse(null);
        assertNotNull(approvedLecture);
        assertEquals("강의 예정", approvedLecture.getLectureStatus().getCodeName());
    }

    //강의실 중복체크 테스트
    @Test
    public void lectureTimeDuplicationTest() {
        // given
        LectureDTO lectureDTO = new LectureDTO();
        lectureDTO.setFacilityId(4L);
        lectureDTO.setLectureDays("MONDAY");
        lectureDTO.setStartTime("PERIOD_SECOND");
        lectureDTO.setEndTime("PERIOD_FOURTH");

        // when & then
        assertDoesNotThrow(() -> {
            boolean isDuplicated = lectureService.isLectureTimeDuplicated(lectureDTO, 1);
            assertFalse(isDuplicated, "강의실 시간이 중복되지 않아야 합니다.");
        });
    }
}
