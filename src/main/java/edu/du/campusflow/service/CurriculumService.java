package edu.du.campusflow.service;

import edu.du.campusflow.entity.Curriculum;
import edu.du.campusflow.entity.CurriculumSubject;
import edu.du.campusflow.entity.Subject;
import edu.du.campusflow.repository.CurriculumRepository;
import edu.du.campusflow.repository.CurriculumSubjectRepository;
import edu.du.campusflow.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurriculumService {

   private final CurriculumRepository curriculumRepository;
   private final CurriculumSubjectRepository curriculumSubjectRepository;
   private final SubjectRepository subjectRepository;

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
   public void createCurriculum(Curriculum curriculum, Long subjectId, Long prereqSubjectId) {
      // 교육과정 저장
      Curriculum savedCurriculum = curriculumRepository.save(curriculum);

      // 과목 추가
      if (subjectId != null) {
         Subject subject = subjectRepository.findById(subjectId)
             .orElseThrow(() -> new IllegalArgumentException("Invalid subject ID: " + subjectId));

         CurriculumSubject curriculumSubject = new CurriculumSubject();
         curriculumSubject.setCurriculum(savedCurriculum);
         curriculumSubject.setSubject(subject);

         // 선수강 과목 추가 (옵션)
         if (prereqSubjectId != null) {
            Subject prereqSubject = subjectRepository.findById(prereqSubjectId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid prereq subject ID: " + prereqSubjectId));
            curriculumSubject.setPrereqSubject(prereqSubject);
         }

         curriculumSubjectRepository.save(curriculumSubject);
      }
   }
}

