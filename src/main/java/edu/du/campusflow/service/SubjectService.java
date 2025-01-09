package edu.du.campusflow.service;

import edu.du.campusflow.entity.Subject;
import edu.du.campusflow.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectService {

   private final SubjectRepository subjectRepository;

   public List<Subject> searchSubjects(String keyword) {
      if (keyword == null || keyword.isEmpty()) {
         return List.of(); // 검색어가 없으면 빈 리스트 반환
      }
      return subjectRepository.findBySubjectNameContaining(keyword);
   }
}
