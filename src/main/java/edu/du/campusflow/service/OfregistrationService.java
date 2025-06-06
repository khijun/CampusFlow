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
import java.util.Optional;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
@Transactional
public class OfregistrationService {

    private final LectureRepository lectureRepository;
    private final OfregistrationRepository ofregistrationRepository;
    private final LectureTimeRepository lectureTimeRepository;
    private final CommonCodeRepository commonCodeRepository;
    private final MemberRepository memberRepository;
    private final HttpSession httpSession;  // HttpSession 주입
    private final AuthService authService;  // AuthService 주입
    private final CompletionService completionService;  // CompletionService 주입

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
        Long currentStudentId = authService.getCurrentMemberId();
        List<Map<String, Object>> results = ofregistrationRepository.findAllAvailableLecturesForStudent(currentStudentId);
        List<OfregistrationDTO> dtos = new ArrayList<>();

        for (Map<String, Object> row : results) {
            // 다른 학과의 전공 필수 과목인 경우 건너뛰기
            if (!row.get("deptName").toString().equals(studentDeptName) &&
                    row.get("subjectType").toString().equals("전공 필수")) {
                continue;
            }

            OfregistrationDTO dto = new OfregistrationDTO();
            dto.setLectureId(((Number) row.get("lectureId")).longValue());
            dto.setLectureName((String) row.get("lectureName"));
            dto.setDeptName((String) row.get("deptName"));
            dto.setSubjectType((String) row.get("subjectType"));
            dto.setGrade((String) row.get("grade"));
            dto.setSubjectCredits((Integer) row.get("subjectCredits"));
            dto.setMemberId(((Number) row.get("professorId")).longValue());
            dto.setName((String) row.get("professorName"));
            
            // 강의 시간 및 장소 정보가 있는 경우에만 설정
            if (row.get("lectureDay") != null) {
                dto.setLectureDay((String) row.get("lectureDay"));
                dto.setStartTime((String) row.get("startTime"));
                dto.setEndTime((String) row.get("endTime"));
                dto.setFacilityName((String) row.get("facilityName"));
            }

            dto.setMaxStudents((Integer) row.get("maxStudents"));
            dto.setCurrentStudents((Integer) row.get("currentStudents"));
            dto.setDayNight((String) row.get("dayNight"));
            dto.setRegStatus((String) row.get("regStatus"));

            dtos.add(dto);
        }

        // 정렬 로직은 그대로 유지
        dtos.sort((a, b) -> {
            if (!a.getDeptName().equals(studentDeptName) && b.getDeptName().equals(studentDeptName)) return 1;
            if (a.getDeptName().equals(studentDeptName) && !b.getDeptName().equals(studentDeptName)) return -1;
            if (a.getDeptName().equals(studentDeptName) && b.getDeptName().equals(studentDeptName)) {
                if (a.getSubjectType().equals("전공 필수") && !b.getSubjectType().equals("전공 필수")) return -1;
                if (!a.getSubjectType().equals("전공 필수") && b.getSubjectType().equals("전공 필수")) return 1;
            }
            return a.getDeptName().compareTo(b.getDeptName());
        });

        return dtos;
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
            // 거절된 강의는 시간 중복 체크에서 제외
            if (existingReg.getRegStatus().getCodeValue().equals("REJECTED")) {
                continue;
            }
            
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
                    // 교시를 숫자로 변환
                    int newStart = convertPeriodToNumber(newTime.getStartTime().getCodeValue());
                    int newEnd = convertPeriodToNumber(newTime.getEndTime().getCodeValue());
                    int existingStart = convertPeriodToNumber(existingTime.getStartTime().getCodeValue());
                    int existingEnd = convertPeriodToNumber(existingTime.getEndTime().getCodeValue());

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

    public List<OfregistrationDTO> getStudentRegistrations(Long memberId) {
        List<Map<String, Object>> results = ofregistrationRepository.findStudentRegistrationsOptimized(memberId);
        List<OfregistrationDTO> dtos = new ArrayList<>();

        for (Map<String, Object> row : results) {
            OfregistrationDTO dto = new OfregistrationDTO();
            
            // 기본 강의 정보 설정
            dto.setLectureId(((Number) row.get("lectureId")).longValue());
            dto.setLectureName((String) row.get("lectureName"));
            dto.setDeptName((String) row.get("deptName"));
            dto.setSubjectType((String) row.get("subjectType"));
            dto.setGrade((String) row.get("grade"));
            dto.setSubjectCredits((Integer) row.get("subjectCredits"));

            // 교수 정보 설정
            dto.setMemberId(((Number) row.get("professorId")).longValue());
            dto.setName((String) row.get("professorName"));

            // 강의 시간 및 장소 정보 설정
            if (row.get("lectureDay") != null) {
                dto.setLectureDay((String) row.get("lectureDay"));
                dto.setStartTime((String) row.get("startTime"));
                dto.setEndTime((String) row.get("endTime"));
                dto.setFacilityName((String) row.get("facilityName"));
            }

            // 수강 인원 정보 설정
            dto.setMaxStudents((Integer) row.get("maxStudents"));
            dto.setCurrentStudents((Integer) row.get("currentStudents"));
            
            // 기타 정보 설정
            dto.setDayNight((String) row.get("dayNight"));
            dto.setRegStatus((String) row.get("regStatus")); // code_name을 직접 사용

            dtos.add(dto);
        }

        return dtos;
    }

    /**
     * 수강신청 대기 상태인 강의 목록 조회
     */
    public List<OfregistrationDTO> getPendingRegistrations() {
        List<Map<String, Object>> results = ofregistrationRepository.findAllPendingRegistrationsForAdmin();
        List<OfregistrationDTO> dtos = new ArrayList<>();

        for (Map<String, Object> row : results) {
            OfregistrationDTO dto = new OfregistrationDTO();
            
            // 기본 강의 정보 설정
            dto.setLectureId(((Number) row.get("lectureId")).longValue());
            dto.setLectureName((String) row.get("lectureName"));
            dto.setDeptName((String) row.get("deptName"));
            dto.setSubjectType((String) row.get("subjectType"));
            dto.setGrade((String) row.get("grade"));
            dto.setSubjectCredits((Integer) row.get("subjectCredits"));

            // 학생 정보 설정
            dto.setMemberId(((Number) row.get("studentId")).longValue());
            dto.setName((String) row.get("studentName"));

            // 강의 시간 및 장소 정보 설정
            if (row.get("lectureDay") != null) {
                dto.setLectureDay((String) row.get("lectureDay"));
                dto.setStartTime((String) row.get("startTime"));
                dto.setEndTime((String) row.get("endTime"));
                dto.setFacilityName((String) row.get("facilityName"));
            }

            // 수강 인원 정보 설정
            dto.setMaxStudents((Integer) row.get("maxStudents"));
            dto.setCurrentStudents((Integer) row.get("currentStudents"));
            
            // 기타 정보 설정
            dto.setDayNight((String) row.get("dayNight"));
            dto.setRegStatus((String) row.get("regStatus"));

            dtos.add(dto);
        }

        return dtos;
    }

    /**
     * 수강신청 상태 업데이트
     */
    @Transactional
    public void updateRegistrationStatus(Long lectureId, Long memberId, String status) {
        // 수강신청 정보 조회
        Ofregistration registration = ofregistrationRepository
                .findByLectureIdAndMemberId(lectureId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("수강신청 정보를 찾을 수 없습니다."));

        // 상태 코드 조회
        CommonCode statusCode = commonCodeRepository.findByCodeValue(status);
        String currentStatus = registration.getRegStatus().getCodeValue();

        // 현재 상태가 승인이 아니고, 새로운 상태가 승인일 경우에만 수강인원 증가
        if (!"APPROVED".equals(currentStatus) && "APPROVED".equals(status)) {
            Lecture lecture = registration.getLectureId();
            lecture.setCurrentStudents(lecture.getCurrentStudents() + 1);
            lectureRepository.save(lecture);
            
            // Completion 생성
            completionService.createCompletion(registration);
        }
        // 현재 상태가 승인이고, 새로운 상태가 거절일 경우 수강인원 감소
        else if ("APPROVED".equals(currentStatus) && "REJECTED".equals(status)) {
            Lecture lecture = registration.getLectureId();
            lecture.setCurrentStudents(lecture.getCurrentStudents() - 1);
            lectureRepository.save(lecture);
        }

        // 상태 업데이트
        registration.setRegStatus(statusCode);
        ofregistrationRepository.save(registration);
    }

    @Transactional
    public void cancelRegistration(Long lectureId) {
        // 세션에서 현재 로그인한 사용자 ID 가져오기
        Long memberId = authService.getCurrentMemberId();
        if (memberId == null) {
            throw new IllegalStateException("로그인 정보를 찾을 수 없습니다.");
        }

        // 수강신청 정보가 존재하는지 확인
        Optional<Ofregistration> registration = ofregistrationRepository.findByLectureIdAndMemberId(lectureId, memberId);
        if (registration.isEmpty()) {
            throw new IllegalStateException("수강신청 정보를 찾을 수 없습니다.");
        }

        try {
            // 수강신청 정보 삭제
            ofregistrationRepository.deleteByLectureIdAndMemberMemberId(lectureId, memberId);
        } catch (Exception e) {
            throw new RuntimeException("수강신청 취소 중 오류가 발생했습니다.", e);
        }
    }
}