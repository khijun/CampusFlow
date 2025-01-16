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

   /**
    * 검색어 기반으로 과목을 조회하거나, 검색어가 없으면 전체 과목 조회.
    *
    * @param keyword 검색어 (nullable)
    * @return 과목 리스트
    */
   public List<Subject> findSubjects(String keyword) {
      if (keyword != null && !keyword.isEmpty()) {
         return searchSubjects(keyword); // 검색어 기반 조회
      }
      return subjectRepository.findAll(); // 전체 과목 조회
   }

   /**
    * 검색어로 과목을 조회.
    *
    * @param keyword 검색어
    * @return 검색된 과목 리스트
    */
   public List<Subject> searchSubjects(String keyword) {
      return subjectRepository.findBySubjectNameContaining(keyword);
   }
}
