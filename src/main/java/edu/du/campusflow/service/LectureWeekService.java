package edu.du.campusflow.service;

import edu.du.campusflow.dto.LectureDTO;
import edu.du.campusflow.dto.LectureWeekDTO;
import edu.du.campusflow.entity.*;
import edu.du.campusflow.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LectureWeekService {

    @Autowired
    LectureWeekRepository lectureWeekRepository;

    @Autowired
    LectureRepository lectureRepository;

    @Autowired
    CommonCodeRepository commonCodeRepository;

    @Autowired
    LectureTimeRepository lectureTimeRepository;

    @Autowired
    CommonCodeGroupRepository commonCodeGroupRepository;


    public List<LectureWeekDTO> getLecturesWeekTime(String semesterCode, String professorId, String lectureName) {
        List<LectureWeekDTO> result = new ArrayList<>();

        // 강의 조회
        List<Lecture> lectures = lectureRepository.findAll((root, query, cb) -> {
            // 조건 설정
            CommonCode pendingStatus = commonCodeRepository.findByCodeValue("LECTURE_PENDING");
            CommonCode startLectures = commonCodeRepository.findByCodeValue("LECTURE_STARTED");

            return cb.and(
                    cb.equal(root.get("member").get("memberId"), professorId),
                    cb.like(root.get("lectureName"), "%" + lectureName + "%"),
                    cb.equal(root.get("semester").get("codeValue"), semesterCode),
                    cb.or(
                            cb.equal(root.get("lectureStatus"), pendingStatus),
                            cb.equal(root.get("lectureStatus"), startLectures)
                    )
            );
        });

        // 각 강의에 대해 주차와 시간 정보 조회
        for (Lecture lecture : lectures) {
            List<LectureTime> lectureTimes = lectureTimeRepository.findByLectureWeek_Lecture(lecture);
            for (LectureTime time : lectureTimes) {
                LectureWeek week = time.getLectureWeek();
                LectureWeekDTO dto = new LectureWeekDTO();
                dto.setLectureId(lecture.getLectureId());
                dto.setLectureName(lecture.getLectureName());
                dto.setSemesterName(lecture.getSemester().getCodeName());
                dto.setLectureTimeId(time.getLectureTimeId());
                dto.setLectureWeekId(week.getLectureWeekId());
                dto.setLectureWeekName(week.getLectureWeekName());
                dto.setStartTime(time.getStartTime().getCodeName());
                dto.setEndTime(time.getEndTime().getCodeName());
                dto.setLectureDays(time.getLectureDay().getCodeName());
                if (time.getFacility() != null) {
                    dto.setFacilityName(time.getFacility().getFacilityName());
                }
                if (time.getClassStatus() != null) {
                    dto.setClassStatus(time.getClassStatus().getCodeName());
                }
                result.add(dto);
            }
        }
        return result;
    }

    //페이지 로드될때 강의명 드롭다운으로 불러오는 서비스
    public List<LectureDTO> getProfessorLectures(String professorId, String semesterCode) {
        return lectureRepository.findAll((root, query, criteriaBuilder) -> {
                    CommonCode pendingStatus = commonCodeRepository.findByCodeValue("LECTURE_PENDING");
                    CommonCode startLectures = commonCodeRepository.findByCodeValue("LECTURE_STARTED");

                    return criteriaBuilder.and(
                            criteriaBuilder.equal(root.get("member").get("memberId"), professorId),
                            criteriaBuilder.equal(root.get("semester").get("codeValue"), semesterCode),
                            criteriaBuilder.or(
                                    criteriaBuilder.equal(root.get("lectureStatus"), pendingStatus),
                                    criteriaBuilder.equal(root.get("lectureStatus"), startLectures)
                            )
                    );
                }).stream()
                .map(lecture -> {
                    LectureDTO dto = new LectureDTO();
                    dto.setLectureId(lecture.getLectureId());
                    dto.setLectureName(lecture.getLectureName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    //강의 상태 드롭다운에 상태 데이터 불러오는 서비스
    public List<CommonCode> getClassStatusCodes() {
        CommonCodeGroup classStatusGroup = commonCodeGroupRepository.findByGroupCode("CLASSSTATUS");
        System.out.println("ClassStatusGroup: " + classStatusGroup);

        if (classStatusGroup == null) {
            return Collections.emptyList();
        }

        List<CommonCode> statusCodes = commonCodeRepository.findByCodeGroup(classStatusGroup);
        System.out.println("Found status codes: " + statusCodes.size());
        statusCodes.forEach(code ->
                System.out.println("Code: " + code.getCodeValue() + " - " + code.getCodeName())
        );
        return statusCodes;
    }

    //수업 상태 변경하는 코드
    public void updateLectureStatus(Long lectureTimeId, String classStatus) {
        LectureTime lectureTime = lectureTimeRepository.findById(lectureTimeId)
                .orElseThrow(() -> new RuntimeException("해당 강의 시간을 찾을 수 없습니다."));

        // 상태 검증
        validateLectureTimeStatus(lectureTime, classStatus);

        CommonCode statusCode = commonCodeRepository.findByCodeValue(classStatus);
        if (statusCode == null) {
            throw new RuntimeException("유효하지 않은 상태 코드입니다.");
        }

        lectureTime.setClassStatus(statusCode);
        lectureTimeRepository.save(lectureTime);
        // 종료나 휴강으로 변경 시 다음 주차 강의 시간 생성
        if ("LECTURE_COMPLETED".equals(classStatus) || "CANCELLED".equals(classStatus)) {
            LectureWeek currentWeek = lectureTime.getLectureWeek();
            Lecture lecture = currentWeek.getLecture();

            // 다음 주차 찾기
            int nextWeekNum = currentWeek.getWeek() + 1;
            List<LectureWeek> weeks = lectureWeekRepository.findByLecture(lecture);
            LectureWeek nextWeek = weeks.stream()
                    .filter(week -> week.getWeek() == nextWeekNum)
                    .findFirst()
                    .orElse(null);

            // 다음 주차가 있고, 아직 시간이 생성되지 않은 경우에만 생성
            if (nextWeek != null && !lectureTimeRepository.existsByLectureWeek(nextWeek)) {
                LectureTime nextTime = new LectureTime();
                nextTime.setLectureWeek(nextWeek);
                nextTime.setLectureDay(lectureTime.getLectureDay());    // 같은 요일
                nextTime.setStartTime(lectureTime.getStartTime());      // 같은 시작 시간
                nextTime.setEndTime(lectureTime.getEndTime());          // 같은 종료 시간
                nextTime.setFacility(lectureTime.getFacility());        // 같은 강의실
                nextTime.setClassStatus(commonCodeRepository.findByCodeValue("SCHEDULED")); // 예정 상태로 설정
                lectureTimeRepository.save(nextTime);
            }
        }
    }


    //수업 상태 검증
    private void validateLectureTimeStatus(LectureTime lectureTime, String classStatus) {
        // 현재 수업 상태가 '종료'인지 확인
        if (lectureTime.getClassStatus() != null &&
                "LECTURE_COMPLETED".equals(lectureTime.getClassStatus().getCodeValue())) {
            throw new RuntimeException("이미 종료된 수업은 상태를 변경할 수 없습니다.");
        }

        // 현재 상태가 '예정' 상태가 아닐 때 '예정' 상태로 변경하려는 경우 체크
        if (lectureTime.getClassStatus() != null &&
                !"SCHEDULED".equals(lectureTime.getClassStatus().getCodeValue()) &&
                "SCHEDULED".equals(classStatus)) {
            throw new RuntimeException("진행중이거나 휴강인 강의를 예정 으로 변경할 수 없습니다");
        }

        //휴강일때 진행 상태로 변경하지 못하게 체크
        if(lectureTime.getClassStatus() != null &&
                "CANCELLED".equals(lectureTime.getClassStatus().getCodeValue())) {
            throw new RuntimeException("휴강인 강의의 상태를 변경할수 없습니다");
        }

        //진행중인 강의일때 휴강 상태로 변경하지 못하게 체크
        if(lectureTime.getClassStatus() != null &&
                "ONGOING".equals(lectureTime.getClassStatus().getCodeValue()) &&
                "CANCELLED".equals(classStatus)) {
            throw new RuntimeException("진행중인 강의는 휴강상태로 변경할수 없습니다");
        }

        CommonCode statusCode = commonCodeRepository.findByCodeValue(classStatus);
        if (statusCode == null) {
            throw new RuntimeException("유효하지 않은 상태 코드입니다.");
        }
    }
}