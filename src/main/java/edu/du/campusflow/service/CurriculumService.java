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
                 // ✅ 디버깅 로그 추가
                 log.info("저장할 DTO: {}", dto);

                 Curriculum curriculum = convertToEntity(dto);

                 // ✅ 각 필드가 NULL인지 확인
                 if (curriculum.getDept() == null) {
                    log.error("❌ Dept가 NULL입니다!");
                 }
                 if (curriculum.getGrade() == null) {
                    log.error("❌ Grade가 NULL입니다!");
                 }
                 if (curriculum.getCurriculumStatus() == null) {
                    log.error("❌ CurriculumStatus가 NULL입니다!");
                 }
                 if (curriculum.getSemester() == null) {
                    log.error("❌ Semester가 NULL입니다!");
                 }
                 if (curriculum.getDayNight() == null) {
                    log.error("❌ DayNight가 NULL입니다!");
                 }

                 return curriculum;
              })
              .collect(Collectors.toList());

      curriculumRepository.saveAll(curriculums);
   }

   // Curriculum -> CurriculumDTO 변환
   private CurriculumDTO convertToDTO(Curriculum curriculum) {
      CurriculumDTO dto = new CurriculumDTO();
      dto.setCurriculumId(curriculum.getCurriculumId());

      // ✅ dept가 null인지 확인 후 처리
      if (curriculum.getDept() != null) {
         dto.setDeptId(curriculum.getDept().getDeptId());
         dto.setDeptName(curriculum.getDept().getDeptName());
      } else {
         dto.setDeptId(null);
         dto.setDeptName("미정"); // 기본값 설정
      }

      dto.setCurriculumName(curriculum.getCurriculumName());
      dto.setCurriculumYear(curriculum.getCurriculumYear());
      dto.setCreatedAt(curriculum.getCreatedAt());
      dto.setUpdatedAt(curriculum.getUpdatedAt());

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
              .grade(commonCodeService.getCodeByValue(24L, dto.getGrade())) // GRADE (group_id: 24)
              .curriculumStatus(commonCodeService.getCodeByValue(7L, dto.getCurriculumStatus())) // CURRICULUMSTATUS (group_id: 7)
              .semester(commonCodeService.getCodeByValue(22L, dto.getSemester())) // SEMESTER (group_id: 22)
              .dayNight(commonCodeService.getCodeByValue(28L, dto.getDayNight())) // DAY_NIGHT (group_id: 28)
              .reason(dto.getReason())
              .build();
   }

}
