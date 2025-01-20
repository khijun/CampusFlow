package edu.du.campusflow.service;

import edu.du.campusflow.dto.CurriculumDTO;
import edu.du.campusflow.entity.Curriculum;
import edu.du.campusflow.entity.Dept;
import edu.du.campusflow.repository.CurriculumRepository;
import edu.du.campusflow.repository.DeptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CurriculumService {
   private final CurriculumRepository curriculumRepository;
   private final DeptRepository deptRepository;
   private final CommonCodeService commonCodeService;
   private static final Logger log = LoggerFactory.getLogger(CurriculumService.class);

   // 교육과정 전체 조회 또는 검색 조회
   public List<CurriculumDTO> getCurriculums(String keyword) {
      List<Curriculum> curriculums = (keyword != null && !keyword.trim().isEmpty()) ?
              curriculumRepository.findByCurriculumNameContaining(keyword) :
              curriculumRepository.findAll();

      return curriculums.stream()
              .map(this::convertToDTO)
              .collect(Collectors.toList());
   }

   // 교육과정 저장
   public void saveCurriculums(List<CurriculumDTO> curriculumDTOs) {
      List<Curriculum> curriculums = curriculumDTOs.stream()
              .map(dto -> {
                 Curriculum curriculum = convertToEntity(dto);
                 return curriculum;
              })
              .collect(Collectors.toList());

      curriculumRepository.saveAll(curriculums);
   }

   // Curriculum -> CurriculumDTO 변환
   private CurriculumDTO convertToDTO(Curriculum curriculum) {
      CurriculumDTO dto = new CurriculumDTO();
      dto.setCurriculumId(curriculum.getCurriculumId());

      if (curriculum.getDept() != null) {
         dto.setDeptId(curriculum.getDept().getDeptId());
         dto.setDeptName(curriculum.getDept().getDeptName());
      } else {
         dto.setDeptId(null);
         dto.setDeptName("미정"); // 기본값 설정
      }

      dto.setCurriculumName(curriculum.getCurriculumName());
      dto.setCurriculumYear(curriculum.getCurriculumYear());

      // createdAt, updatedAt이 null인지 확인
      if (curriculum.getCreatedAt() != null) {
         dto.setCreatedAt(curriculum.getCreatedAt());
      } else {
         dto.setCreatedAt(LocalDateTime.now()); // 기본값 설정
      }

      if (curriculum.getUpdatedAt() != null) {
         dto.setUpdatedAt(curriculum.getUpdatedAt());
      } else {
         dto.setUpdatedAt(LocalDateTime.now()); // 기본값 설정
      }

      // 공통 코드: CodeName으로 변경
      dto.setGrade(curriculum.getGrade() != null ? curriculum.getGrade().getCodeName() : "미정");
      dto.setCurriculumStatus(curriculum.getCurriculumStatus() != null ? curriculum.getCurriculumStatus().getCodeName() : "미정");
      dto.setSemester(curriculum.getSemester() != null ? curriculum.getSemester().getCodeName() : "미정");
      dto.setDayNight(curriculum.getDayNight() != null ? curriculum.getDayNight().getCodeName() : "미정");

      dto.setReason(curriculum.getReason());
      return dto;
   }

   private Curriculum convertToEntity(CurriculumDTO dto) {
      Dept dept = deptRepository.findById(dto.getDeptId()).orElseThrow(() ->
              new IllegalArgumentException("존재하지 않는 학과 ID: " + dto.getDeptId()));

      return Curriculum.builder()
              .dept(dept)
              .curriculumName(dto.getCurriculumName())
              .curriculumYear(dto.getCurriculumYear())
              .createdAt(LocalDateTime.now())
              .updatedAt(LocalDateTime.now())
              .grade(commonCodeService.getCodeByValue(24L, dto.getGrade()))
              .curriculumStatus(commonCodeService.getCodeByValue(7L, dto.getCurriculumStatus()))
              .semester(commonCodeService.getCodeByValue(22L, dto.getSemester()))
              .dayNight(commonCodeService.getCodeByValue(28L, dto.getDayNight()))
              .reason(dto.getReason())
              .build();
   }

   @Transactional
   public void updateCurriculums(List<CurriculumDTO> updatedCurriculums) {
      for (CurriculumDTO dto : updatedCurriculums) {
         Curriculum curriculum = curriculumRepository.findById(dto.getCurriculumId())
                 .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 교육과정 ID: " + dto.getCurriculumId()));

         curriculum.setCurriculumName(dto.getCurriculumName());
         curriculum.setCurriculumYear(dto.getCurriculumYear());

         // 공통 코드 매칭 (codeName → codeValue 변환)
         String gradeValue = dto.getGrade() != null ? dto.getGrade() : "GRADE_1"; // 기본값 설정
         String statusValue = dto.getCurriculumStatus() != null ? dto.getCurriculumStatus() : "ACTIVE";
         String semesterValue = dto.getSemester() != null ? dto.getSemester() : "FIRST_SEMESTER";
         String dayNightValue = dto.getDayNight() != null ? dto.getDayNight() : "DAY";

         curriculum.setGrade(commonCodeService.getCodeByValue(24L, gradeValue));
         curriculum.setCurriculumStatus(commonCodeService.getCodeByValue(7L, statusValue));
         curriculum.setSemester(commonCodeService.getCodeByValue(22L, semesterValue));
         curriculum.setDayNight(commonCodeService.getCodeByValue(28L, dayNightValue));

         curriculum.setReason(dto.getReason());

         curriculumRepository.save(curriculum);
      }
   }

   @Transactional
   public void deleteCurriculums(List<Long> curriculumIds) {
      curriculumRepository.deleteAllById(curriculumIds);
   }

}
