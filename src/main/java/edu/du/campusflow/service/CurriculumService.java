package edu.du.campusflow.service;

import edu.du.campusflow.dto.CurriculumDTO;
import edu.du.campusflow.entity.*;
import edu.du.campusflow.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurriculumService {

   private final CurriculumRepository curriculumRepository;
   private final CurriculumSubjectRepository curriculumSubjectRepository;
   private final SubjectRepository subjectRepository;
   private final CommonCodeGroupRepository commonCodeGroupRepository;
   private final DeptRepository deptRepository;

   // 검색 메서드
   public List<Curriculum> searchCurriculum(String year, String grade, String deptName, String curriculumName, String category) {
      Specification<Curriculum> spec = Specification.where(null);

      if (year != null && !year.isEmpty()) {
         spec = spec.and((root, query, cb) -> cb.equal(root.get("curriculumYear"), Integer.parseInt(year)));
      }
      if (grade != null && !grade.isEmpty()) {
         spec = spec.and((root, query, cb) -> cb.equal(root.get("grade").get("codeValue"), grade));
      }
      if (deptName != null && !deptName.isEmpty()) {
         spec = spec.and((root, query, cb) -> cb.like(root.get("dept").get("deptName"), "%" + deptName + "%"));
      }
      if (curriculumName != null && !curriculumName.isEmpty()) {
         spec = spec.and((root, query, cb) -> cb.like(root.get("curriculumName"), "%" + curriculumName + "%"));
      }
      if (category != null && !category.isEmpty()) {
         spec = spec.and((root, query, cb) -> cb.equal(root.get("curriculumStatus").get("codeValue"), category));
      }

      return curriculumRepository.findAll(spec);
   }

   @Transactional
   public Curriculum createCurriculum(CurriculumDTO dto) {
      Curriculum curriculum = new Curriculum();
      curriculum.setDept(findDepartmentById(dto.getDeptId()));
      curriculum.setCurriculumName(dto.getCurriculumName());
      curriculum.setCurriculumYear(dto.getYear());
      curriculum.setGradeCapacity(dto.getGradeCapacity());
      curriculum.setCreatedAt(LocalDateTime.now());
      curriculum.setUpdatedAt(LocalDateTime.now());
      curriculum.setReason(dto.getReason());

      curriculum.setCurriculumStatus(findCommonCode("CURRICULUMSTATUS", dto.getCurriculumStatus()));
      curriculum.setGrade(findCommonCode("GRADE", dto.getGrade()));
      curriculum.setDayNight(findCommonCode("DAY_NIGHT", dto.getDayNight()));
      curriculum.setGradingMethod(findCommonCode("GRADING_METHOD", dto.getGradingMethod()));

      // Curriculum 저장 후 반환
      return curriculumRepository.save(curriculum);
   }

   @Transactional
   public void addCurriculumSubject(Long curriculumId, Long subjectId, Long prereqSubjectId, String semester, String subjectType) {
      Curriculum curriculum = curriculumRepository.findById(curriculumId)
          .orElseThrow(() -> new IllegalArgumentException("Invalid curriculum ID: " + curriculumId));

      CurriculumSubject curriculumSubject = new CurriculumSubject();
      curriculumSubject.setCurriculum(curriculum);

      curriculumSubject.setSubject(findSubjectById(subjectId));

      if (prereqSubjectId != null) {
         curriculumSubject.setPrereqSubject(findSubjectById(prereqSubjectId));
      }

      if (semester != null && !semester.isEmpty()) {
         curriculumSubject.setSemester(findCommonCode("SEMESTER", semester));
      }

      if (subjectType != null && !subjectType.isEmpty()) {
         curriculumSubject.setSubjectType(findCommonCode("SUBJECTTYPE", subjectType));
      }

      curriculumSubjectRepository.save(curriculumSubject);
   }

   private Dept findDepartmentById(Long id) {
      return deptRepository.findById(id) // id로 수정
          .orElseThrow(() -> new IllegalArgumentException("Invalid deptId: " + id)); // id로 수정
   }

   private Subject findSubjectById(Long id) {
      return subjectRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid subject ID: " + id));
   }

   public CommonCode findCommonCode(String groupCode, String codeValue) {
      CommonCodeGroup group = commonCodeGroupRepository.findByGroupCode(groupCode);
      if (group == null) {
         throw new IllegalArgumentException("Invalid group code: " + groupCode);
      }

      return group.getCommonCodes().stream()
          .filter(code -> code.getCodeValue().equals(codeValue))
          .findFirst()
          .orElseThrow(() -> new IllegalArgumentException("Invalid code value: " + codeValue));
   }
}
