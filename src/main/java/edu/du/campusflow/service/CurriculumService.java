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
import java.util.Objects;
import java.util.stream.Collectors;

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

   // ID로 Curriculum 조회
   @Transactional
   public CurriculumDTO getCurriculumById(Long id) {
      Curriculum curriculum = curriculumRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 교육과정 ID: " + id));

      CurriculumDTO dto = new CurriculumDTO();
      dto.setDeptId(curriculum.getDept().getDeptId());
      dto.setCurriculumName(curriculum.getCurriculumName());
      dto.setYear(curriculum.getCurriculumYear());
//      dto.setGradeCapacity(curriculum.getGradeCapacity());
      dto.setCurriculumStatus(curriculum.getCurriculumStatus().getCodeValue());
      dto.setGrade(curriculum.getGrade().getCodeValue());
      dto.setDayNight(curriculum.getDayNight().getCodeValue());
//      dto.setGradingMethod(curriculum.getGradingMethod().getCodeValue());
      dto.setReason(curriculum.getReason());
      return dto;
   }

   @Transactional
   public Curriculum createCurriculum(CurriculumDTO dto) {
      if (dto.getSubjectIds() == null || dto.getSubjectIds().isEmpty()) {
         throw new IllegalArgumentException("Subject IDs are missing.");
      }

      // null 값을 제거
      List<Long> filteredSubjectIds = dto.getSubjectIds().stream()
          .filter(Objects::nonNull)
          .collect(Collectors.toList());

      if (filteredSubjectIds.isEmpty()) {
         throw new IllegalArgumentException("All provided Subject IDs are null.");
      }

      // Curriculum 생성
      Curriculum curriculum = new Curriculum();
      curriculum.setDept(findDepartmentById(dto.getDeptId()));
      curriculum.setCurriculumName(dto.getCurriculumName());
      curriculum.setCurriculumYear(dto.getYear());
//      curriculum.setGradeCapacity(dto.getGradeCapacity());
      curriculum.setCurriculumStatus(findCommonCode("CURRICULUMSTATUS", dto.getCurriculumStatus()));
      curriculum.setGrade(findCommonCode("GRADE", dto.getGrade()));
      curriculum.setDayNight(findCommonCode("DAY_NIGHT", dto.getDayNight()));
//      curriculum.setGradingMethod(findCommonCode("GRADING_METHOD", dto.getGradingMethod()));
      curriculum.setReason(dto.getReason());
      curriculum.setCreatedAt(LocalDateTime.now());
      curriculum.setUpdatedAt(LocalDateTime.now());

      curriculumRepository.save(curriculum);

      // CurriculumSubject 생성
      for (int i = 0; i < dto.getSubjectIds().size(); i++) {
         CurriculumSubject curriculumSubject = new CurriculumSubject();
         curriculumSubject.setCurriculum(curriculum);
         curriculumSubject.setSubject(findSubjectById(dto.getSubjectIds().get(i)));
//         curriculumSubject.setSemester(findCommonCode("SEMESTER", dto.getSemesters().get(i))); // 각 과목별 학기 설정
         curriculumSubjectRepository.save(curriculumSubject);
      }

      return curriculum;
   }


   @Transactional
   public void addCurriculumSubject(Long curriculumId, Long subjectId, String semester) {
      // 1. Curriculum 검증
      Curriculum curriculum = curriculumRepository.findById(curriculumId)
          .orElseThrow(() -> new IllegalArgumentException("Invalid curriculum ID: " + curriculumId));

      // 2. Subject 검증
      Subject subject = findSubjectById(subjectId);

      // 3. CurriculumSubject 생성
      CurriculumSubject curriculumSubject = new CurriculumSubject();
      curriculumSubject.setCurriculum(curriculum);
      curriculumSubject.setSubject(subject);

      // 4. 학기 설정 (유효성 확인)
      if (semester != null && !semester.isEmpty()) {
         CommonCode semesterCode = findCommonCode("SEMESTER", semester);
//         curriculumSubject.setSemester(semesterCode);
      } else {
         throw new IllegalArgumentException("Semester cannot be null or empty");
      }

      // 5. 저장
      curriculumSubjectRepository.save(curriculumSubject);
   }

   private Dept findDepartmentById(Long id) {
      return deptRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid deptId: " + id));
   }

   private Subject findSubjectById(Long id) {
      return subjectRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Invalid subjectId: " + id));
   }

   public CommonCode findCommonCode(String groupCode, String codeValue) {
      if (codeValue == null || codeValue.isEmpty()) {
         throw new IllegalArgumentException("Code value is missing for group: " + groupCode);
      }

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
