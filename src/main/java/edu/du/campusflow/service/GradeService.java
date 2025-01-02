package edu.du.campusflow.service;

import edu.du.campusflow.entity.Grade;
import edu.du.campusflow.repository.GradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepository gradeRepository;
//    private final CompletionRepository completionRepository;

    // 1. 모든 성적 조회
    public List<Grade> view_all_grades() {
        return gradeRepository.findAll();
    }

//    // 2. 특정 completion_id에 해당하는 성적 조회
//    public List<Grade> view_grades_by_completion(Long completionId) {
//        Completion completion = completionRepository.findById(completionId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 completion_id에 대한 이수 정보 없음"));
//
//        return gradeRepository.findByCompletion(completion);  // completion_id로 성적 조회
//    }
//
//    // 3. 성적 추가하기 (Builder 패턴 사용)
//    public Grade add_grade(Grade grade) {
//        Completion completion = completionRepository.findById(grade.getCompletion().getCompletionId())
//                .orElseThrow(() -> new IllegalArgumentException("해당 completion_id에 대한 이수 정보 없음"));
//
//        return gradeRepository.save(Grade.builder()
//                .completion(completion)   // completion_id 참조
//                .gradeType(grade.getGradeType())         // grade_type
//                .score(grade.getScore())   // score
//                .build());
//    }
//
//    // 4. 성적 수정하기 (Builder 패턴 사용)
//    public Grade update_grade(Long gradeId, Grade updatedGrade) {
//        Grade existingGrade = gradeRepository.findById(gradeId)
//                .orElseThrow(() -> new IllegalArgumentException("성적 정보 없음"));
//
//        Completion completion = completionRepository.findById(updatedGrade.getCompletion().getCompletionId())
//                .orElseThrow(() -> new IllegalArgumentException("해당 completion_id에 대한 이수 정보 없음"));
//
//        Grade updatedGradeEntity = Grade.builder()
//                .gradeId(gradeId)
//                .completion(completion)
//                .gradeType(updatedGrade.getGradeType())
//                .score(updatedGrade.getScore())
//                .build();
//
//        return gradeRepository.save(updatedGradeEntity);  // 수정된 성적 정보 저장
//    }
//
//    // 5. 성적 삭제하기
//    public void delete_grade(Long gradeId) {
//        Grade existingGrade = gradeRepository.findById(gradeId)
//                .orElseThrow(() -> new IllegalArgumentException("성적 정보 없음"));
//        gradeRepository.delete(existingGrade);  // 성적 정보 삭제
//    }
//
//    // 6. 성적 평균 구하기
//    public double calculate_average_score(Long completionId) {
//        List<Grade> grades = view_grades_by_completion(completionId);
//        if (grades.isEmpty()) {
//            throw new IllegalArgumentException("해당 completionId에 대한 성적이 존재하지 않음");
//        }
//
//        return grades.stream()
//                .mapToInt(Grade::getScore)
//                .average()
//                .orElseThrow(() -> new IllegalArgumentException("성적 계산 오류"));
//    }

}

