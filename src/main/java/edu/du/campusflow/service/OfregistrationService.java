package edu.du.campusflow.service;

import edu.du.campusflow.dto.OfregistrationDTO;
import edu.du.campusflow.entity.*;
import edu.du.campusflow.repository.LectureRepository;
import edu.du.campusflow.repository.LectureTimeRepository;
import edu.du.campusflow.repository.OfregistrationRepository;
import edu.du.campusflow.repository.CommonCodeRepository;
import edu.du.campusflow.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OfregistrationService {

    private final LectureRepository lectureRepository;
    private final OfregistrationRepository ofregistrationRepository;
    private final LectureTimeRepository lectureTimeRepository;
    private final CommonCodeRepository commonCodeRepository;
    private final MemberRepository memberRepository;

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
        List<Lecture> lectures = lectureRepository.findAllWithFetch();
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

    // 수강신청 처리 메서드 추가
    public void registerLecture(OfregistrationDTO ofregistrationDTO) {
        // 1. 강의 정보 조회
        Lecture lecture = lectureRepository.findById(ofregistrationDTO.getLectureId())
                .orElseThrow(() -> new IllegalArgumentException("해당 강의를 찾을 수 없습니다."));

        // 2. 회원 정보 조회
        Member member = memberRepository.findById(ofregistrationDTO.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        // 3. 수강신청 상태 코드 조회 (REQUESTED)
        CommonCode regStatus = commonCodeRepository.findByCodeValue("REQUESTED");

        // 4. 수강 신청 정보 생성
        Ofregistration ofregistration = Ofregistration.builder()
                .lectureId(lecture)
                .member(member)
                .regDate(LocalDate.now())
                .regStatus(regStatus)
                .retake(false)
                .build();

        // 5. 저장
        ofregistrationRepository.save(ofregistration);

        // 6. 강의 현재 수강인원 증가
        lecture.setCurrentStudents(lecture.getCurrentStudents() + 1);
        lectureRepository.save(lecture);
    }
}