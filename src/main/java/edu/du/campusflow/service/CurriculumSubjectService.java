package edu.du.campusflow.service;

import edu.du.campusflow.dto.CurriculumSubjectDTO;
import edu.du.campusflow.entity.CurriculumSubject;
import edu.du.campusflow.repository.CurriculumSubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CurriculumSubjectService {

    @Autowired
    CurriculumSubjectRepository curriculumSubjectRepository;

    //강좌 개설에서 사용할 교육과정 교과목 검색
    public List<CurriculumSubjectDTO> searchCurriculumSubjectBySubjectName(String subjectName, String curriculumName) {
        List<CurriculumSubject> curriculumSubjects = curriculumSubjectRepository.findAll((root, query, criteriaBuilder) -> {
            if (subjectName != null && !subjectName.isEmpty()) {
                return criteriaBuilder.like(root.get("subject").get("subjectName"), "%" + subjectName + "%");
            }
            if (curriculumName != null && !curriculumName.isEmpty()) {
                return criteriaBuilder.like(root.get("curriculum").get("curriculumName"), "%" + curriculumName + "%");
            }
            return criteriaBuilder.conjunction(); // 기본 조건
        });

        return curriculumSubjects.stream().map(curriculumSubject -> {
            CurriculumSubjectDTO dto = new CurriculumSubjectDTO();
            dto.setCurriculumSubjectId(curriculumSubject.getCurriculumSubjectId()); // 추가
            dto.setSubjectName(curriculumSubject.getSubject().getSubjectName());
            dto.setCurriculumName(curriculumSubject.getCurriculum() != null ? curriculumSubject.getCurriculum().getCurriculumName() : null);
            dto.setSubjectCredits(curriculumSubject.getSubject() != null ? curriculumSubject.getSubject().getSubjectCredits().toString() : null);
            dto.setSemesterName(curriculumSubject.getSemester() != null ? curriculumSubject.getSemester().getCodeName() : null); // 학기 이름으로 설정
            dto.setSubjectTypeName(curriculumSubject.getSubjectType() != null ? curriculumSubject.getSubjectType().getCodeName() : null); // 코드 이름으로 설정
            return dto;
        }).collect(Collectors.toList());
    }

}
