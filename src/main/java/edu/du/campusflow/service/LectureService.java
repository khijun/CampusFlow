package edu.du.campusflow.service;

import edu.du.campusflow.dto.LectureDTO;
import edu.du.campusflow.entity.*;
import edu.du.campusflow.enums.LectureStatus;
import edu.du.campusflow.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.criteria.Predicate;

@Service
public class LectureService {

    @Autowired
    LectureRepository lectureRepository;

    @Autowired
    CurriculumSubjectRepository curriculumSubjectRepository;

    @Autowired
    LectureWeekRepository lectureWeekRepository;

    @Autowired
    LectureTimeRepository lectureTimeRepository;

    @Autowired
    CommonCodeRepository commonCodeRepository;

    @Autowired
    AuthService authService;

    @Autowired
    FacilityRepository facilityRepository;

    //강의 개설
    public void createLecture(LectureDTO request) {
        // 필수 입력 필드 검증
        if(request.getCurriculumSubjectId() == null ) {
            throw  new IllegalArgumentException("교과목을 선택해주세요");
        }

        if (request.getLectureName() == null || request.getLectureName().isEmpty()) {
            throw new IllegalArgumentException("강의명을 입력해주세요.");
        }

        if (request.getMaxStudents() == null || request.getMaxStudents() <= 0) {
            throw new IllegalArgumentException("최대 수강 인원을 입력해주세요.");
        }



        // 현재 로그인한 교수 정보 가져오기
        Member professor = authService.getCurrentMember();
        if (professor == null) {
            throw new RuntimeException("로그인 정보를 찾을 수 없습니다.");
        }

        CurriculumSubject curriculumSubject = curriculumSubjectRepository.findById(request.getCurriculumSubjectId())
                .orElseThrow(() -> new RuntimeException("교과목을 찾을 수 없습니다."));

        // 강의 객체를 생성합니다.
        Lecture lecture = new Lecture();
        lecture.setLectureName(request.getLectureName());
        lecture.setMaxStudents(request.getMaxStudents());
        lecture.setCurrentStudents(0);
        lecture.setCurriculumSubject(curriculumSubject);
        lecture.setMember(professor);

        // 학기 정보 설정
        CommonCode semester = curriculumSubject.getSemester();
        lecture.setSemester(semester);

        CommonCode lectureStatus = commonCodeRepository.findByCodeValue("APPROVAL_PENDING");
        if (lectureStatus == null) {
            throw new RuntimeException("강의 상태를 찾을 수 없습니다.");
        }
        lecture.setLectureStatus(lectureStatus);

        // 강의 저장
        lectureRepository.save(lecture);
    }

    //강의 승인 및 강의실 배정 페이지 에서 사용할 검색
    public List<LectureDTO> searchLectures(String semesterCode, String deptName, String professorName) {
        List<Lecture> lectures = lectureRepository.findAll((root, query, criteriaBuilder) -> {
            // 승인 대기 상태 조건
            CommonCode pendingStatus = commonCodeRepository.findByCodeValue("APPROVAL_PENDING");

            // 두 조건이 모두 있는 경우
            if (deptName != null && !deptName.isEmpty()
                    && professorName != null && !professorName.isEmpty()) {
                // 학기 조건 추가
                if (semesterCode != null && !semesterCode.isEmpty()) {
                    return criteriaBuilder.and(
                            criteriaBuilder.equal(root.get("lectureStatus"), pendingStatus), //승인 대기 상태 조건
                            criteriaBuilder.like(root.get("member").get("dept").get("deptName"), "%" + deptName + "%"),
                            criteriaBuilder.like(root.get("member").get("name"), "%" + professorName + "%"),
                            criteriaBuilder.equal(root.get("semester").get("codeValue"), semesterCode)
                    );
                }
            }
            // 학과명만 있는 경우
            if (deptName != null && !deptName.isEmpty()) {
                // 학기 조건 추가
                if (semesterCode != null && !semesterCode.isEmpty()) {
                    return criteriaBuilder.and(
                            criteriaBuilder.equal(root.get("lectureStatus"), pendingStatus),
                            criteriaBuilder.like(root.get("member").get("dept").get("deptName"), "%" + deptName + "%"),
                            criteriaBuilder.equal(root.get("semester").get("codeValue"), semesterCode)
                    );
                }
            }
            // 교수명만 있는 경우
            if (professorName != null && !professorName.isEmpty()) {
                // 학기 조건 추가
                if (semesterCode != null && !semesterCode.isEmpty()) {
                    return criteriaBuilder.and(
                            criteriaBuilder.equal(root.get("lectureStatus"), pendingStatus),
                            criteriaBuilder.like(root.get("member").get("name"), "%" + professorName + "%"),
                            criteriaBuilder.equal(root.get("semester").get("codeValue"), semesterCode)
                    );
                }
            }
            // 학기 조건 추가
            if (semesterCode != null && !semesterCode.isEmpty()) {
                return criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("lectureStatus"), pendingStatus),
                        criteriaBuilder.equal(root.get("semester").get("codeValue"), semesterCode)
                );
            }
            return criteriaBuilder.equal(root.get("lectureStatus"), pendingStatus);
        });

        return lectures.stream()
                .map(lecture -> {
                    LectureDTO dto = new LectureDTO();
                    dto.setLectureId(lecture.getLectureId());
                    dto.setLectureName(lecture.getLectureName());
                    dto.setProfessorName(lecture.getMember().getName());
                    dto.setMaxStudents(lecture.getMaxStudents());
                    dto.setDeptName(lecture.getMember().getDept().getDeptName());
                    dto.setLectureStatus(lecture.getLectureStatus().getCodeName());
                    dto.setSemesterName(lecture.getSemester().getCodeName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void approveLecture(LectureDTO lectureDTO) {
        // 강의 조회
        if (lectureDTO.getLectureId() == null) {
            throw new IllegalArgumentException("강의 ID가 제공되지 않았습니다.");
        }

        Lecture lecture = lectureRepository.findById(lectureDTO.getLectureId())
                .orElseThrow(() -> new RuntimeException("강의를 찾을 수 없습니다."));

        // 강의 상태를 승인으로 변경
        CommonCode approvedStatus = commonCodeRepository.findByCodeValue("LECTURE_PENDING");
        if (approvedStatus == null) {
            throw new RuntimeException("승인 상태 코드를 찾을 수 없습니다.");
        }
        lecture.setLectureStatus(approvedStatus);
        
        // 강의실 조회
        if (lectureDTO.getFacilityId() == null) {
            throw new IllegalArgumentException("강의실 ID가 제공되지 않았습니다.");
        }

        Facility facility = facilityRepository.findById(lectureDTO.getFacilityId())
                .orElseThrow(() -> new RuntimeException("강의실을 찾을 수 없습니다."));

        // 강의실 수용 인원 체크
        if (lecture.getMaxStudents() > facility.getCapacity()) {
            throw new RuntimeException("강의 최대 인원이 강의실 수용 인원을 초과합니다.");
        }

        CommonCode classStatus = commonCodeRepository.findByCodeValue("SCHEDULED");
        if (classStatus == null) {
            throw new RuntimeException("수업 상태 코드를 찾을 수 없습니다");
        }

        // 선택한 주차만큼 강의 주차와 강의 시간 데이터 생성 (1주차부터)
        for (int i = 1; i <= lectureDTO.getWeek(); i++) {
            // 강의 주차 생성
            LectureWeek lectureWeek = new LectureWeek();
            lectureWeek.setLecture(lecture);
            lectureWeek.setWeek(i);
            lectureWeek.setLectureWeekName(i+"주차");
            lectureWeek = lectureWeekRepository.save(lectureWeek);

            // 해당 주차의 강의 시간 생성
            LectureTime lectureTime = new LectureTime();
            lectureTime.setLectureWeek(lectureWeek);
            lectureTime.setLectureDay(commonCodeRepository.findByCodeValue(lectureDTO.getLectureDays()));
            lectureTime.setStartTime(commonCodeRepository.findByCodeValue(lectureDTO.getStartTime()));
            lectureTime.setEndTime(commonCodeRepository.findByCodeValue(lectureDTO.getEndTime()));
            lectureTime.setFacility(facility);
            lectureTime.setClassStatus(classStatus);

            // 강의실과 일정 중복 체크
            if (isLectureTimeDuplicated(lectureDTO, i)) {
                throw new RuntimeException("선택한 강의실과 일정에 이미 다른 강의가 있습니다.");
            }

            lectureTimeRepository.save(lectureTime);
        }

        // 변경된 강의 저장
        lectureRepository.save(lecture);
    }

    //강의 주차 교시 생성시 중복 체크
    public boolean isLectureTimeDuplicated(LectureDTO lectureDTO, int week) {
        CommonCode startTime = commonCodeRepository.findByCodeValue(lectureDTO.getStartTime());
        CommonCode endTime = commonCodeRepository.findByCodeValue(lectureDTO.getEndTime());

        List<LectureTime> lectureTimes = lectureTimeRepository.findByStartTimeAndEndTimeAndLectureWeek_Week(startTime, endTime, week);

        for (LectureTime lectureTime : lectureTimes) {
            if (lectureTime.getLectureWeek().getLecture().getLectureId().equals(lectureDTO.getLectureId())) {
                continue;  // 같은 강의는 건너뜀
            }
            if (lectureTime.getLectureDay().getCodeValue().equals(lectureDTO.getLectureDays())) {
                if (lectureTime.getFacility().getFacilityId().equals(lectureDTO.getFacilityId())) {
                    return true;  // 중복
                }
            }
        }
        return false;  // 중복 아님
    }

    public List<LectureDTO> getApprovedLectures(String semesterCode, String professorId) {
        List<Lecture> lectures = lectureRepository.findAll((root, query, criteriaBuilder) -> {

            CommonCode pendingStatus = commonCodeRepository.findByCodeValue("LECTURE_PENDING");
            CommonCode startLectures = commonCodeRepository.findByCodeValue("LECTURE_STARTED");

            if(professorId != null) {
                if (semesterCode != null && !semesterCode.isEmpty()) {
                    return criteriaBuilder.and(
                            criteriaBuilder.equal(root.get("member").get("memberId"), professorId),
                            criteriaBuilder.equal(root.get("semester").get("codeValue"), semesterCode),
                            criteriaBuilder.or(
                                    criteriaBuilder.equal(root.get("lectureStatus"), pendingStatus),
                                    criteriaBuilder.equal(root.get("lectureStatus"), startLectures)
                            )
                    );
                } else {
                    return criteriaBuilder.and(
                            criteriaBuilder.equal(root.get("member").get("memberId"), professorId),
                            criteriaBuilder.or(
                                    criteriaBuilder.equal(root.get("lectureStatus"), pendingStatus),
                                    criteriaBuilder.equal(root.get("lectureStatus"), startLectures)
                            )
                    );
                }
            }

            return criteriaBuilder.or(
                    criteriaBuilder.equal(root.get("lectureStatus"), pendingStatus),
                    criteriaBuilder.equal(root.get("lectureStatus"), startLectures)
            );
        });

        return lectures.stream()
                .map(lecture -> {
                    LectureDTO dto = new LectureDTO();
                    dto.setLectureId(lecture.getLectureId());
                    dto.setLectureName(lecture.getLectureName());
                    dto.setSubjectCredits(lecture.getCurriculumSubject().getSubject().getSubjectCredits());
                    dto.setMaxStudents(lecture.getMaxStudents());
                    dto.setCurrentStudents(lecture.getCurrentStudents());
                    dto.setSemesterName(lecture.getSemester().getCodeName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<LectureDTO> getPendingLectures(String semesterCode, String professorId) {
        List<Lecture> lectures = lectureRepository.findAll((root, query, criteriaBuilder) -> {
            CommonCode pendingStatus = commonCodeRepository.findByCodeValue("APPROVAL_PENDING");

            if (professorId != null) {
                        if(semesterCode != null && !semesterCode.isEmpty()) {
                            return criteriaBuilder.and(
                                    criteriaBuilder.equal(root.get("semester").get("codeValue"), semesterCode),
                                    criteriaBuilder.equal(root.get("member").get("memberId"), professorId),
                                    criteriaBuilder.equal(root.get("lectureStatus"), pendingStatus)
                            );
                        }
            }

            return criteriaBuilder.equal(root.get("lectureStatus"), pendingStatus);
        });

        return lectures.stream()
                .map(lecture -> {
                    LectureDTO dto = new LectureDTO();
                    dto.setLectureId(lecture.getLectureId());
                    dto.setLectureName(lecture.getLectureName());
                    dto.setMaxStudents(lecture.getMaxStudents());
                    dto.setSubjectCredits(lecture.getCurriculumSubject().getSubject().getSubjectCredits());
                    dto.setSemesterName(lecture.getSemester().getCodeName());
                    dto.setLectureStatus(lecture.getLectureStatus().getCodeName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

}
