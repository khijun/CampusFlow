package edu.du.campusflow.service;

import edu.du.campusflow.entity.Curriculum;
import edu.du.campusflow.repository.CurriculumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurriculumService {

   private final CurriculumRepository curriculumRepository;

   // 특정 ID로 Curriculum 조회
   public Curriculum getCurriculumById(Long curriculumId) {
      return curriculumRepository.findById(curriculumId)
          .orElseThrow(() -> new IllegalArgumentException("Curriculum not found with ID: " + curriculumId));
   }

   // Curriculum 저장
   public Curriculum saveCurriculum(Curriculum curriculum) {
      return curriculumRepository.save(curriculum);
   }

   // Curriculum 삭제
   public void deleteCurriculum(Long curriculumId) {
      if (!curriculumRepository.existsById(curriculumId)) {
         throw new IllegalArgumentException("Cannot delete. Curriculum not found with ID: " + curriculumId);
      }
      curriculumRepository.deleteById(curriculumId);
   }
}
