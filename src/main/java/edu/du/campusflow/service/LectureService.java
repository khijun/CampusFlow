package edu.du.campusflow.service;

import edu.du.campusflow.dto.LectureDTO;
import edu.du.campusflow.entity.*;
import edu.du.campusflow.enums.LectureStatus;
import edu.du.campusflow.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    MemberRepository memberRepository;

    @Autowired
    AuthService authService;

    //강의 개설
    public void createLecture(LectureDTO request) {
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

        CommonCode lectureStatus = commonCodeRepository.findByCodeValue("LECTURE_PENDING");
        if (lectureStatus == null) {
            throw new RuntimeException("강의 상태를 찾을 수 없습니다.");
        }
        lecture.setLectureStatus(lectureStatus);

        // 강의 저장
        lectureRepository.save(lecture);
    }

    //강의 승인 및 강의실 배정 페이지 에서 사용할 검색
    public List<LectureDTO> searchLectures(String deptName, String professorName) {
        List<Lecture> lectures = lectureRepository.findAll((root, query, criteriaBuilder) -> {
            // 승인 대기 상태 조건
            CommonCode pendingStatus = commonCodeRepository.findByCodeValue("LECTURE_PENDING");

            // 두 조건이 모두 있는 경우
            if (deptName != null && !deptName.isEmpty()
                    && professorName != null && !professorName.isEmpty()) {
                return criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("lectureStatus"), pendingStatus),  // 승인 대기 상태 조건 추가
                        criteriaBuilder.like(root.get("member").get("dept").get("deptName"),
                                "%" + deptName + "%"),
                        criteriaBuilder.like(root.get("member").get("name"),
                                "%" + professorName + "%")
                );
            }
            // 학과명만 있는 경우
            if (deptName != null && !deptName.isEmpty()) {
                return criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("lectureStatus"), pendingStatus),  // 승인 대기 상태 조건 추가
                        criteriaBuilder.like(root.get("member").get("dept").get("deptName"),
                                "%" + deptName + "%")
                );
            }
            // 교수명만 있는 경우
            if (professorName != null && !professorName.isEmpty()) {
                return criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("lectureStatus"), pendingStatus),  // 승인 대기 상태 조건 추가
                        criteriaBuilder.like(root.get("member").get("name"),
                                "%" + professorName + "%")
                );
            }
            // 검색 조건이 없는 경우 승인 대기 상태만 조회
            return criteriaBuilder.equal(root.get("lectureStatus"), pendingStatus);
        });

        return lectures.stream()
                .map(lecture -> {
                    LectureDTO dto = new LectureDTO();
                    dto.setLectureName(lecture.getLectureName());
                    dto.setProfessorName(lecture.getMember().getName());
                    dto.setMaxStudents(lecture.getMaxStudents());
                    dto.setDeptName(lecture.getMember().getDept().getDeptName());
                    dto.setLectureStatus(lecture.getLectureStatus().getCodeName());
                    return dto;
                })
                .collect(Collectors.toList());
    }




}
