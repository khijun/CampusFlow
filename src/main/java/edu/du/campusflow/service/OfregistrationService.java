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
    public List<OfregistrationDTO> getAllAvailableLectures(String studentDeptName) {
        List<Lecture> lectures = lectureRepository.findAllWithFetch();
        List<OfregistrationDTO> result = new ArrayList<>();

        for (Lecture lecture : lectures) {
            CurriculumSubject curriculumSubject = lecture.getCurriculumSubject();
            Curriculum curriculum = curriculumSubject.getCurriculum();
            Subject subject = curriculumSubject.getSubject();
            Member professor = lecture.getMember();
            List<LectureTime> lectureTimes = lectureTimeRepository.findByLectureWeek_Lecture(lecture);

            // 다른 학과의 전공 필수 과목인 경우 건너뛰기
            if (!curriculum.getDept().getDeptName().equals(studentDeptName) && 
                curriculumSubject.getSubjectType().getCodeValue().equals("MAJOR_REQUIRED")) {
                continue;
            }

            OfregistrationDTO dto = new OfregistrationDTO();
            
            // 기존 DTO 설정 코드는 그대로 유지
            dto.setLectureId(lecture.getLectureId());
            dto.setLectureName(lecture.getLectureName());
            dto.setDeptName(curriculum.getDept().getDeptName());
            dto.setSubjectType(curriculumSubject.getSubjectType().getCodeValue());
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

            result.add(dto);
        }

        // 학과명과 과목 유형으로 정렬
        result.sort((a, b) -> {
            // 먼저 본인 학과인지 확인
            if (!a.getDeptName().equals(studentDeptName) && b.getDeptName().equals(studentDeptName)) return 1;
            if (a.getDeptName().equals(studentDeptName) && !b.getDeptName().equals(studentDeptName)) return -1;
            
            // 같은 학과인 경우 전공필수 여부로 정렬
            if (a.getDeptName().equals(studentDeptName) && b.getDeptName().equals(studentDeptName)) {
                if (a.getSubjectType().equals("전공 필수") && !b.getSubjectType().equals("전공 필수")) return -1;
                if (!a.getSubjectType().equals("전공 필수") && b.getSubjectType().equals("전공 필수")) return 1;
            }
            
            // 그 외의 경우 학과명으로 정렬
            return a.getDeptName().compareTo(b.getDeptName());
        });

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

        // 3. 중복 신청 체크
        boolean alreadyRegistered = ofregistrationRepository
                .existsByMemberAndLectureId(member, lecture);
        if (alreadyRegistered) {
            throw new IllegalStateException("이미 신청한 강의입니다.");
        }

        // 5. 시간 중복 체크
        List<Ofregistration> studentLectures = ofregistrationRepository.findByMember(member);
        for (Ofregistration existingReg : studentLectures) {
            // 학생이 이미 신청한 강의 정보 가져오기
            Lecture existingLecture = existingReg.getLectureId();
            
            // 기존 강의와 새로 신청하려는 강의의 시간 정보 조회
            List<LectureTime> existingTimes = lectureTimeRepository.findByLectureWeek_Lecture(existingLecture);
            List<LectureTime> newTimes = lectureTimeRepository.findByLectureWeek_Lecture(lecture);
            
            // 두 강의 모두 시간 정보가 있는 경우에만 체크
            if (!existingTimes.isEmpty() && !newTimes.isEmpty()) {
                // 각 강의의 첫 번째 시간표 정보를 가져옴
                LectureTime existingTime = existingTimes.get(0);
                LectureTime newTime = newTimes.get(0);
                
                // 같은 요일에 진행되는 강의인 경우에만 시간 중복 체크
                if (existingTime.getLectureDay().getCodeValue().equals(newTime.getLectureDay().getCodeValue())) {
                    // 교시를 숫자로 변환 (예: PERIOD_FIRST -> 1, PERIOD_SECOND -> 2)
                    int newStart = convertPeriodToNumber(newTime.getStartTime().getCodeValue());
                    int newEnd = convertPeriodToNumber(newTime.getEndTime().getCodeValue());
                    int existingStart = convertPeriodToNumber(existingTime.getStartTime().getCodeValue());
                    int existingEnd = convertPeriodToNumber(existingTime.getEndTime().getCodeValue());

                    // 시간이 겹치는지 확인
                    // (새 강의 시작 시간이 기존 강의 종료 시간보다 이르고,
                    //  새 강의 종료 시간이 기존 강의 시작 시간보다 늦은 경우 겹침)
                    if (hasTimeOverlap(newStart, newEnd, existingStart, existingEnd)) {
                        throw new IllegalStateException(
                            String.format("'%s' 강의와 시간이 겹칩니다.", existingLecture.getLectureName())
                        );
                    }
                }
            }
        }

        // 5. 수강신청 상태 코드 조회 (REQUESTED)
        CommonCode regStatus = commonCodeRepository.findByCodeValue("REQUESTED");

        // 6. 수강 신청 정보 생성 및 저장
        Ofregistration ofregistration = Ofregistration.builder()
                .lectureId(lecture)
                .member(member)
                .regDate(LocalDate.now())
                .regStatus(regStatus)
                .retake(false)
                .build();

        ofregistrationRepository.save(ofregistration);

        // 7. 강의 현재 수강인원 증가
        lecture.setCurrentStudents(lecture.getCurrentStudents() + 1);
        lectureRepository.save(lecture);
    }

    // 교시를 숫자로 변환하는 메서드
    private int convertPeriodToNumber(String period) {
        switch (period) {
            case "PERIOD_FIRST":
                return 1;
            case "PERIOD_SECOND":
                return 2;
            case "PERIOD_THIRD":
                return 3;
            case "PERIOD_FOURTH":
                return 4;
            case "PERIOD_FIFTH":
                return 5;
            case "PERIOD_SIXTH":
                return 6;
            case "PERIOD_SEVENTH":
                return 7;
            case "PERIOD_EIGHTH":
                return 8;
            case "PERIOD_NINTH":
                return 9;
            default:
                throw new IllegalArgumentException("Invalid period: " + period);
        }
    }

    // 시간 겹침을 확인하는 메서드
    private boolean hasTimeOverlap(int start1, int end1, int start2, int end2) {
        return start1 <= end2 && end1 >= start2;
    }
}