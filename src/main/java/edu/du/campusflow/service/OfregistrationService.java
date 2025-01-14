package edu.du.campusflow.service;

import edu.du.campusflow.dto.OfregistrationDTO;
import edu.du.campusflow.entity.*;
import edu.du.campusflow.repository.LectureRepository;
import edu.du.campusflow.repository.LectureTimeRepository;
import edu.du.campusflow.repository.OfregistrationRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OfregistrationService {

    private final LectureRepository lectureRepository;
    private final OfregistrationRepository ofregistrationRepository;
    private final LectureTimeRepository lectureTimeRepository;

    public Ofregistration getOfregistrationById(Long id) {
        return ofregistrationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("수강신청 정보를 찾을 수 없습니다. ID: " + id));


    }

    /**
     * 수강 가능한 모든 강의 목록을 조회
     * 각 강의의 상세 정보(강의명, 담당교수, 강의시간 등)를 포함
     *
     * @return 수강 가능한 강의 목록 (OfregistrationDTO 형태)
     */
    public List<OfregistrationDTO> getAllAvailableLectures() {
        // 모든 강의 정보를 데이터베이스에서 조회
        List<Lecture> lectures = lectureRepository.findAll();
        List<OfregistrationDTO> result = new ArrayList<>();

        // 각 강의 정보를 DTO로 변환
        for (Lecture lecture : lectures) {
            // 강의 기본 정보 추출
            CurriculumSubject curriculumSubject = lecture.getCurriculumSubject();
            Curriculum curriculum = curriculumSubject.getCurriculum();
            Subject subject = curriculumSubject.getSubject();
            Member professor = lecture.getMember();
            List<LectureTime> lectureTimes = lectureTimeRepository.findByLectureWeek_Lecture(lecture);

            // DTO 생성 및 데이터 설정
            OfregistrationDTO dto = new OfregistrationDTO();

            // 강의 기본 정보 설정
            dto.setLectureId(lecture.getLectureId());
            dto.setLectureName(lecture.getLectureName());
            dto.setDeptName(curriculum.getDept().getDeptName());
            dto.setGrade(curriculum.getGrade().getCodeName());
            dto.setSubjectType(curriculumSubject.getSubjectType().getCodeName());
            dto.setSubjectCredits(subject.getSubjectCredits());

            // 교수 정보 설정
            dto.setMemberId(professor.getMemberId());
            dto.setName(professor.getName());

            // 강의 시간 및 장소 정보 설정
            if (!lectureTimes.isEmpty()) {
                LectureTime lectureTime = lectureTimes.get(0);
                dto.setLectureDay(lectureTime.getLectureDay().getCodeName());
                dto.setStartTime(lectureTime.getStartTime().getCodeName());
                dto.setEndTime(lectureTime.getEndTime().getCodeName());
                dto.setFacilityName(lectureTime.getFacility().getFacilityName());
            }

            // 수강 인원 정보 설정
            dto.setMaxStudents(lecture.getMaxStudents());
            dto.setCurrentStudents(lecture.getCurrentStudents());

            // 기타 정보 설정
            dto.setDayNight(curriculum.getDayNight().getCodeName());

            // 결과 리스트에 추가
            result.add(dto);
        }

        return result;
    }
}