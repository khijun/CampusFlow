package edu.du.campusflow.service;

import edu.du.campusflow.dto.SubjectDTO;
import edu.du.campusflow.entity.Subject;
import edu.du.campusflow.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubjectService {

   private final SubjectRepository subjectRepository;

   public List<Subject> findSubjects(String keyword) {
      if (keyword != null && !keyword.isEmpty()) {
         return searchSubjects(keyword); // 검색어 기반 조회
      }
      return subjectRepository.findAll(); // 전체 과목 조회
   }

   //검색어로 과목조회
   public List<Subject> searchSubjects(String keyword) {
      return subjectRepository.findBySubjectNameContaining(keyword);
   }

   public void saveAll(List<SubjectDTO> subjectDTOs) {
      List<Subject> subjects = subjectDTOs.stream()
          .map(dto -> new Subject(null, dto.getSubjectName(), dto.getSubjectDesc(), dto.getSubjectCredits()))
          .collect(Collectors.toList());
      subjectRepository.saveAll(subjects);
   }

   public void updateSubjects(List<SubjectDTO> updatedSubjects) {
      List<Subject> subjects = updatedSubjects.stream()
          .map(dto -> new Subject(dto.getSubjectId(), dto.getSubjectName(), dto.getSubjectDesc(), dto.getSubjectCredits()))
          .collect(Collectors.toList());
      subjectRepository.saveAll(subjects);
   }

   public void deleteSubjects(List<Long> ids) {
      subjectRepository.deleteAllById(ids); // JPA 메서드로 ID 리스트 기반 삭제
   }

   public List<SubjectDTO> getAllSubjects() {
      List<Subject> subjects = subjectRepository.findAll(Sort.by(Sort.Direction.ASC, "subjectName"));
      return subjects.stream()
          .map(SubjectDTO::fromEntity)
          .collect(Collectors.toList());
   }
}
