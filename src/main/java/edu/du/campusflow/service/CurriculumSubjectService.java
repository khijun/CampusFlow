package edu.du.campusflow.service;

import edu.du.campusflow.dto.CurriculumSubjectDTO;
import edu.du.campusflow.dto.CurriculumSubjectDetailDTO;
import edu.du.campusflow.entity.CurriculumSubject;
import edu.du.campusflow.entity.Subject;
import edu.du.campusflow.repository.CurriculumSubjectRepository;
import edu.du.campusflow.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CurriculumSubjectService {

    @Autowired
    CurriculumSubjectRepository curriculumSubjectRepository;

    @Autowired
    SubjectRepository subjectRepository;

    //강좌 개설에서 사용할 교육과정 교과목 검색
    public List<CurriculumSubjectDTO> searchCurriculumSubjectBySubjectName(String subjectName, String curriculumName, String semesterCode) {
        List<CurriculumSubject> curriculumSubjects = curriculumSubjectRepository.findAll((root, query, criteriaBuilder) -> {
            // 두 조건이 모두 있는 경우
            if (subjectName != null && !subjectName.isEmpty()
                    && curriculumName != null && !curriculumName.isEmpty()) {
                if (semesterCode != null && !semesterCode.isEmpty()) {
                    return criteriaBuilder.and(
                            criteriaBuilder.like(root.get("subject").get("subjectName"), "%" + subjectName + "%"),
                            criteriaBuilder.like(root.get("curriculum").get("curriculumName"), "%" + curriculumName + "%"),
                            criteriaBuilder.equal(root.get("curriculum").get("semester").get("codeValue"), semesterCode)
                    );
                }
                return criteriaBuilder.and(
                        criteriaBuilder.like(root.get("subject").get("subjectName"), "%" + subjectName + "%"),
                        criteriaBuilder.like(root.get("curriculum").get("curriculumName"), "%" + curriculumName + "%")
                );
            }
            //과목명만으로 검색했을 경우
            if (subjectName != null && !subjectName.isEmpty()) {
                if (semesterCode != null && !semesterCode.isEmpty()) {
                    return criteriaBuilder.and(
                            criteriaBuilder.like(root.get("subject").get("subjectName"), "%" + subjectName + "%"),
                            criteriaBuilder.equal(root.get("curriculum").get("semester").get("codeValue"), semesterCode)
                    );
                }
                return criteriaBuilder.like(root.get("subject").get("subjectName"), "%" + subjectName + "%");
            }
            //교육과정명만으로 검색했을경우
            if (curriculumName != null && !curriculumName.isEmpty()) {
                if (semesterCode != null && !semesterCode.isEmpty()) {
                    return criteriaBuilder.and(
                            criteriaBuilder.like(root.get("curriculum").get("curriculumName"), "%" + curriculumName + "%"),
                            criteriaBuilder.equal(root.get("curriculum").get("semester").get("codeValue"), semesterCode)
                    );
                }
                return criteriaBuilder.like(root.get("curriculum").get("curriculumName"), "%" + curriculumName + "%");
            }
            //학기만으로 검색했을 경우
            if (semesterCode != null && !semesterCode.isEmpty()) {
                return criteriaBuilder.equal(root.get("curriculum").get("semester").get("codeValue"), semesterCode);
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
            return dto;
        }).collect(Collectors.toList());
    }

    // 교육과정 교과목 조회
    public List<CurriculumSubjectDetailDTO> getCurriculumSubjects(String keyword) {
        List<CurriculumSubject> subjects = curriculumSubjectRepository.findAll();

        if (keyword != null && !keyword.isEmpty()) {
            subjects = subjects.stream()
                .filter(subject -> subject.getCurriculum().getCurriculumName().contains(keyword))
                .collect(Collectors.toList());
        }

        return subjects.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // 교육과정 교과목 수정
    @Transactional
    public void updateCurriculumSubjects(List<CurriculumSubjectDetailDTO> updatedSubjects) {
        for (CurriculumSubjectDetailDTO dto : updatedSubjects) {
            CurriculumSubject subject = curriculumSubjectRepository.findById(dto.getCurriculumSubjectId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 교육과정 교과목 ID: " + dto.getCurriculumSubjectId()));

            subject.setSubjectType(null);
            subject.setGradingMethod(null);

            curriculumSubjectRepository.save(subject);
        }
    }

    // 교육과정 교과목 삭제
    @Transactional
    public void deleteCurriculumSubjects(List<Long> curriculumSubjectIds) {
        curriculumSubjectRepository.deleteAllById(curriculumSubjectIds);
    }

    // Entity → DTO 변환
    private CurriculumSubjectDetailDTO convertToDTO(CurriculumSubject subject) {
        CurriculumSubjectDetailDTO dto = new CurriculumSubjectDetailDTO();
        dto.setCurriculumSubjectId(subject.getCurriculumSubjectId());
        dto.setCurriculumName(subject.getCurriculum().getCurriculumName());
        dto.setSubjectName(subject.getSubject().getSubjectName());
        dto.setPrereqSubjectName(subject.getPrereqSubject() != null ? subject.getPrereqSubject().getSubjectName() : "미정");
        dto.setSubjectCredits(subject.getSubject().getSubjectCredits() != null ? subject.getSubject().getSubjectCredits().toString() : "미정");
        dto.setSubjectTypeName(subject.getSubjectType() != null ? subject.getSubjectType().getCodeName() : "미정");
        dto.setGradingMethod(subject.getGradingMethod() != null ? subject.getGradingMethod().getCodeName() : "미정");
        dto.setGrade(subject.getCurriculum().getGrade() != null ? subject.getCurriculum().getGrade().getCodeName() : "미정");
        dto.setSemester(subject.getCurriculum().getSemester() != null ? subject.getCurriculum().getSemester().getCodeName() : "미정");
        dto.setDayNight(subject.getCurriculum().getDayNight() != null ? subject.getCurriculum().getDayNight().getCodeName() : "미정");
        dto.setCurriculumStatus(subject.getCurriculum().getCurriculumStatus() != null ? subject.getCurriculum().getCurriculumStatus().getCodeName() : "미정");
        return dto;
    }

}
