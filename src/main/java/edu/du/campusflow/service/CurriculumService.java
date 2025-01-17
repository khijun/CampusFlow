package edu.du.campusflow.service;

import edu.du.campusflow.dto.CurriculumDTO;
import edu.du.campusflow.entity.Curriculum;
import edu.du.campusflow.entity.Dept;
import edu.du.campusflow.repository.CurriculumRepository;
import edu.du.campusflow.repository.DeptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CurriculumService {
   private final CurriculumRepository curriculumRepository;
   private final DeptRepository deptRepository;

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
          .map(this::convertToEntity)
          .collect(Collectors.toList());
      curriculumRepository.saveAll(curriculums);
   }

   // Curriculum -> CurriculumDTO 변환
   private CurriculumDTO convertToDTO(Curriculum curriculum) {
      CurriculumDTO dto = new CurriculumDTO();
      dto.setCurriculumId(curriculum.getCurriculumId());
      dto.setDeptId(curriculum.getDept().getDeptId());
      dto.setCurriculumName(curriculum.getCurriculumName());
      dto.setCurriculumYear(curriculum.getCurriculumYear());
      dto.setCreatedAt(curriculum.getCreatedAt());
      dto.setUpdatedAt(curriculum.getUpdatedAt());

      // ✅ 공통 코드: CodeName으로 변경
      dto.setGrade(curriculum.getGrade() != null ? curriculum.getGrade().getCodeName() : null);
      dto.setCurriculumStatus(curriculum.getCurriculumStatus() != null ? curriculum.getCurriculumStatus().getCodeName() : null);
      dto.setSemester(curriculum.getSemester() != null ? curriculum.getSemester().getCodeName() : null);
      dto.setDayNight(curriculum.getDayNight() != null ? curriculum.getDayNight().getCodeName() : null);

      dto.setReason(curriculum.getReason());
      return dto;
   }

   // CurriculumDTO -> Curriculum 변환
   private Curriculum convertToEntity(CurriculumDTO dto) {
      Dept dept = deptRepository.findById(dto.getDeptId()).orElseThrow(() ->
          new IllegalArgumentException("학과 ID를 찾을 수 없습니다: " + dto.getDeptId()));

      return Curriculum.builder()
          .dept(dept)
          .curriculumName(dto.getCurriculumName())
          .curriculumYear(dto.getCurriculumYear())
          .createdAt(LocalDateTime.now()) // 생성 시간 현재 시간으로 설정
          .updatedAt(LocalDateTime.now())
          .reason(dto.getReason())
          .build();
   }
}
