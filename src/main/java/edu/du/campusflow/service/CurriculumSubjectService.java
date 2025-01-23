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
    public List<CurriculumSubjectDTO> searchCurriculumSubjectBySubjectName(String deptName, String curriculumName, String semesterCode, Integer curriculumYear) {
        List<CurriculumSubject> curriculumSubjects = curriculumSubjectRepository.findAll((root, query, criteriaBuilder) -> {
            // 두 조건이 모두 있는 경우
            if (deptName != null && !deptName.isEmpty()
                    && curriculumName != null && !curriculumName.isEmpty()) {
                if (semesterCode != null && !semesterCode.isEmpty()) {
                    return criteriaBuilder.and(
                            criteriaBuilder.like(root.get("curriculum").get("dept").get("deptName"), "%" + deptName + "%"),
                            criteriaBuilder.like(root.get("curriculum").get("curriculumName"), "%" + curriculumName + "%"),
                            criteriaBuilder.equal(root.get("curriculum").get("semester").get("codeValue"), semesterCode),
                            criteriaBuilder.equal(root.get("curriculum").get("curriculumYear"), curriculumYear)
                    );
                }
                return criteriaBuilder.and(
                        criteriaBuilder.like(root.get("curriculum").get("dept").get("deptName"), "%" + deptName + "%"),
                        criteriaBuilder.like(root.get("curriculum").get("curriculumName"), "%" + curriculumName + "%"),
                        criteriaBuilder.equal(root.get("curriculum").get("curriculumYear"), curriculumYear)
                );
            }
            //과목명만으로 검색했을 경우
            if (deptName != null && !deptName.isEmpty()) {
                if (semesterCode != null && !semesterCode.isEmpty()) {
                    return criteriaBuilder.and(
                            criteriaBuilder.like(root.get("curriculum").get("dept").get("deptName"), "%" + deptName + "%"),
                            criteriaBuilder.equal(root.get("curriculum").get("semester").get("codeValue"), semesterCode),
                            criteriaBuilder.equal(root.get("curriculum").get("curriculumYear"), curriculumYear)
                    );
                }
                return criteriaBuilder.and(
                        criteriaBuilder.like(root.get("subject").get("subjectName"), "%" + deptName + "%"),
                        criteriaBuilder.equal(root.get("curriculum").get("curriculumYear"), curriculumYear)
                );
            }
            //교육과정명만으로 검색했을경우
            if (curriculumName != null && !curriculumName.isEmpty()) {
                if (semesterCode != null && !semesterCode.isEmpty()) {
                    return criteriaBuilder.and(
                            criteriaBuilder.like(root.get("curriculum").get("curriculumName"), "%" + curriculumName + "%"),
                            criteriaBuilder.equal(root.get("curriculum").get("semester").get("codeValue"), semesterCode),
                            criteriaBuilder.equal(root.get("curriculum").get("curriculumYear"), curriculumYear)
                    );
                }
                return criteriaBuilder.and(
                        criteriaBuilder.like(root.get("curriculum").get("curriculumName"), "%" + curriculumName + "%"),
                        criteriaBuilder.equal(root.get("curriculum").get("curriculumYear"), curriculumYear)
                );
            }
            //학기만으로 검색했을 경우
            if (semesterCode != null && !semesterCode.isEmpty()) {
                return criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("curriculum").get("semester").get("codeValue"), semesterCode),
                        criteriaBuilder.equal(root.get("curriculum").get("curriculumYear"), curriculumYear)
                );
            }
            return criteriaBuilder.conjunction(); // 기본 조건
        });

        return curriculumSubjects.stream().map(curriculumSubject -> {
            CurriculumSubjectDTO dto = new CurriculumSubjectDTO();
            dto.setCurriculumSubjectId(curriculumSubject.getCurriculumSubjectId()); // 추가
            dto.setSubjectName(curriculumSubject.getSubject().getSubjectName());
            dto.setCurriculumName(curriculumSubject.getCurriculum() != null ? curriculumSubject.getCurriculum().getCurriculumName() : null);
            dto.setSubjectCredits(curriculumSubject.getSubject() != null ? curriculumSubject.getSubject().getSubjectCredits().toString() : null);
            dto.setSemesterName(curriculumSubject.getCurriculum().getSemester() != null ? curriculumSubject.getCurriculum().getSemester().getCodeName() : null); // 학기 이름으로 설정
            dto.setSubjectTypeName(curriculumSubject.getSubjectType() != null ? curriculumSubject.getSubjectType().getCodeName() : null); // 코드 이름으로 설정
            dto.setCurriculumYear(curriculumSubject.getCurriculum().getCurriculumYear());
            dto.setDeptName(curriculumSubject.getCurriculum().getDept().getDeptName());
            return dto;
        }).collect(Collectors.toList());
    }

}
