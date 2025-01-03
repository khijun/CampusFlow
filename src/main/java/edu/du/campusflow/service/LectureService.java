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

    //강의 개설 화면에서 사용할 강의 검색
    public List<LectureDTO> searchLectures(String semester, String subjectType, String lectureName, String deptName) {
        List<Lecture> lectures = lectureRepository.findAll((root, query, criteriaBuilder) -> {
            Specification<Lecture> spec = Specification.where(null);
            // 검색 조건 추가
            if (semester != null && !semester.isEmpty()) {
                spec = spec.and((root1, query1, criteriaBuilder1) ->
                        criteriaBuilder1.equal(root1.get("semester").get("codeValue"), semester));
            }

            if (subjectType != null && !subjectType.isEmpty()) {
                spec = spec.and((root1, query1, criteriaBuilder1) ->
                        criteriaBuilder1.equal(root1.get("curriculumSubject").get("subjectType").get("codeValue"), subjectType));
            }

            if (lectureName != null && !lectureName.isEmpty()) {
                spec = spec.and((root1, query1, criteriaBuilder1) ->
                        criteriaBuilder1.like(root1.get("lectureName"), "%" + lectureName + "%"));
            }

            if (deptName != null && !deptName.isEmpty()) {
                spec = spec.and((root1, query1, criteriaBuilder1) ->
                        criteriaBuilder1.like(root1.get("curriculumSubject").get("curriculum").get("dept").get("deptName"), "%" + deptName + "%"));
            }
            return spec.toPredicate(root, query, criteriaBuilder);
        });

        return lectures.stream().map(lecture -> {
            LectureDTO dto = new LectureDTO();
            dto.setLectureId(lecture.getLectureId());
            dto.setLectureName(lecture.getLectureName());
            dto.setProfessorName(lecture.getMember() != null ? lecture.getMember().getName() : null);
            dto.setSemesterCodeName(lecture.getSemester() != null ? lecture.getSemester().getCodeName() : null); // Code_name으로 변경
            dto.setMaxStudents(lecture.getMaxStudents());
            dto.setSubjectCredits(lecture.getCurriculumSubject() != null ? lecture.getCurriculumSubject().getSubject().getSubjectCredits() : null);
            return dto;
        }).collect(Collectors.toList());
    }

}
