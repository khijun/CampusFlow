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

    public void createLecture(LectureDTO request) {

        CurriculumSubject curriculumSubject = curriculumSubjectRepository.findById(request.getCurriculumSubjectId())
                .orElseThrow(() -> new RuntimeException("교과목을 찾을 수 없습니다."));

        // 과목명과 교육과정명 비교
        if (!curriculumSubject.getSubject().getSubjectName().equals(request.getLectureName())) {
            throw new RuntimeException("과목명이 일치하지 않습니다.");
        }


        // 강의 객체를 생성합니다.
        Lecture lecture = new Lecture();
        lecture.setLectureName(request.getLectureName());
        lecture.setMaxStudents(request.getMaxStudents());
        lecture.setCurrentStudents(0);
        lecture.setCurriculumSubject(curriculumSubject);

        // 학기 정보 설정
        CommonCode semester = curriculumSubject.getSemester(); // CurriculumSubject에서 학기 정보 가져오기
        lecture.setSemester(semester);

        CommonCode lectureStatus = commonCodeRepository.findByCodeValue("PENDING");
        if (lectureStatus == null) {
            throw new RuntimeException("강의 상태를 찾을 수 없습니다.");
        }
        lecture.setLectureStatus(lectureStatus);

        // 강의 저장
        lectureRepository.save(lecture);
    }

}
