package edu.du.campusflow.service;

import edu.du.campusflow.dto.LectureDTO;
import edu.du.campusflow.entity.*;
import edu.du.campusflow.enums.LectureStatus;
import edu.du.campusflow.repository.*;
import edu.du.campusflow.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
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

    @Autowired
    FileUploadService fileUploadService;

    @Autowired
    FileInfoRepository fileInfoRepository;

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
        CommonCode semester = curriculumSubject.getCurriculum().getSemester();
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

    //강의 승인시 강의 주차 및 강의 교시 생성하는 서비스
    @Transactional
    public void approveLecture(LectureDTO lectureDTO) {
        // 강의 조회
        if (lectureDTO.getLectureId() == null) {
            throw new IllegalArgumentException("강의 ID가 제공되지 않았습니다.");
        }

        Lecture lecture = lectureRepository.findById(lectureDTO.getLectureId())
                .orElseThrow(() -> new RuntimeException("강의를 찾을 수 없습니다."));

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

        // 강의실과 일정 중복 체크
        for (int i = 1; i <= lectureDTO.getWeek(); i++) {
            if (isLectureTimeDuplicated(lectureDTO, i)) {
                throw new RuntimeException("선택한 강의실과 일정에 이미 다른 강의가 있습니다.");
            }
        }

        List<LectureWeek> weeks = new ArrayList<>();
        for (int i = 1; i <= lectureDTO.getWeek(); i++) {
            LectureWeek lectureWeek = new LectureWeek();
            lectureWeek.setLecture(lecture);
            lectureWeek.setWeek(i);
            lectureWeek.setLectureWeekName(i+"주차");
            weeks.add(lectureWeek);
        }
        lectureWeekRepository.saveAll(weeks);  // 배치로 한번에 저장

        // 1주차의 강의시간만 생성
        LectureTime firstWeekTime = new LectureTime();
        firstWeekTime.setLectureWeek(weeks.get(0));
        firstWeekTime.setLectureDay(commonCodeRepository.findByCodeValue(lectureDTO.getLectureDays()));
        firstWeekTime.setStartTime(commonCodeRepository.findByCodeValue(lectureDTO.getStartTime()));
        firstWeekTime.setEndTime(commonCodeRepository.findByCodeValue(lectureDTO.getEndTime()));
        firstWeekTime.setFacility(facility);
        firstWeekTime.setClassStatus(classStatus);
        lectureTimeRepository.save(firstWeekTime);

        // 강의 상태 업데이트
        CommonCode approvedStatus = commonCodeRepository.findByCodeValue("LECTURE_PENDING");
        if (approvedStatus == null) {
            throw new RuntimeException("승인 상태 코드를 찾을 수 없습니다.");
        }
        lecture.setLectureStatus(approvedStatus);

        // 변경된 강의 저장
        lectureRepository.save(lecture);
    }

    //강의 주차 교시 생성시 중복 체크
    public boolean isLectureTimeDuplicated(LectureDTO lectureDTO, Integer week) {
        CommonCode startTime = commonCodeRepository.findByCodeValue(lectureDTO.getStartTime());
        CommonCode endTime = commonCodeRepository.findByCodeValue(lectureDTO.getEndTime());

        // CommonCode의 codeName에서 숫자만 추출 (예: "1교시" -> 1)
        Integer newStartHour = Integer.parseInt(startTime.getCodeName().replaceAll("[^0-9]", ""));
        Integer newEndHour = Integer.parseInt(endTime.getCodeName().replaceAll("[^0-9]", ""));

        List<LectureTime> lectureTimes = lectureTimeRepository.findOverlappingTimes(week, lectureDTO.getLectureDays(), lectureDTO.getFacilityId());

        for (LectureTime lectureTime : lectureTimes) {
            if (lectureTime.getLectureWeek().getLecture().getLectureId().equals(lectureDTO.getLectureId())) {
                continue;  // 같은 강의는 건너뜀
            }

            // 기존 강의 시간을 숫자로 변환
            Integer existingStartHour = Integer.parseInt(lectureTime.getStartTime().getCodeName().replaceAll("[^0-9]", ""));
            Integer existingEndHour = Integer.parseInt(lectureTime.getEndTime().getCodeName().replaceAll("[^0-9]", ""));

            if (!(newEndHour < existingStartHour || newStartHour > existingEndHour)) {
                if (lectureTime.getLectureDay().getCodeValue().equals(lectureDTO.getLectureDays()) &&
                        lectureTime.getFacility().getFacilityId().equals(lectureDTO.getFacilityId())) {
                    return true;  // 시간, 요일, 강의실 모두 겹침
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
                    dto.setFileInfo(lecture.getFileInfo() != null ? lecture.getFileInfo().getId() : null);
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

    @Transactional
    public void uploadLectureFile(MultipartFile file, Long lectureId) {
        // 파일이 비어있는지 확인
        if (file.isEmpty()) {
            throw new RuntimeException("파일이 비어있습니다.");
        }

        try {
            // 강의 조회
            Lecture lecture = lectureRepository.findById(lectureId)
                    .orElseThrow(() -> new RuntimeException("해당 강의를 찾을 수 없습니다."));

            // 파일 저장
            FileInfo fileInfo = fileUploadService.saveFile(file);

            // 강의 정보 업데이트
            lecture.setFileInfo(fileInfo);
            lectureRepository.save(lecture);
        } catch (Exception e) {
            throw new RuntimeException("강의계획서 업로드 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 파일 조회 메서드 추가
    public Resource getFileResource(Long fileId) {
        try {
            // FileInfo 조회
            FileInfo fileInfo = fileInfoRepository.findById(fileId)
                    .orElseThrow(() -> new RuntimeException("파일을 찾을 수 없습니다."));

            // 파일 경로 생성
            Path filePath = Paths.get(fileInfo.getFilePath(), FileUtils.getSaveName(fileInfo));
            return new UrlResource(filePath.toUri());
        } catch (Exception e) {
            throw new RuntimeException("파일을 불러오는데 실패했습니다.");
        }
    }

    // 파일 정보 조회 메서드 추가
    public FileInfo getFileInfo(Long fileId) {
        return fileInfoRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("파일을 찾을 수 없습니다."));
    }

}