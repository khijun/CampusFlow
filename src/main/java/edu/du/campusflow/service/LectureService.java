package edu.du.campusflow.service;

import edu.du.campusflow.entity.Lecture;
import edu.du.campusflow.repository.LectureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LectureService {

    @Autowired
    LectureRepository lectureRepository;

    //강의 개설 화면에서 사용할 강의 검색
    public List<Lecture> searchLectures(String semester, String subjectType, String lectureName, String deptName) {
        return lectureRepository.findAll((root, query, criteriaBuilder) -> {
            Specification<Lecture> spec = Specification.where(null);

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
    }

    public Lecture createLecture() {
        return lectureRepository.save(new Lecture());
    }

}
