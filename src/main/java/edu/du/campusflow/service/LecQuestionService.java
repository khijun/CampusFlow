package edu.du.campusflow.service;

import edu.du.campusflow.dto.LecQuestionDTO;
import edu.du.campusflow.entity.LecItem;
import edu.du.campusflow.entity.LecQuestion;
import edu.du.campusflow.entity.Member;
import edu.du.campusflow.entity.Ofregistration;
import edu.du.campusflow.repository.DeptRepository;
import edu.du.campusflow.repository.LecItemRepository;
import edu.du.campusflow.repository.LecQuestionRepository;
import edu.du.campusflow.repository.OfregistrationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LecQuestionService {
    private final DeptRepository deptRepository;
    private final AuthService authService;
    private final OfregistrationRepository ofregistrationRepository;
    private final LecQuestionRepository lecQuestionRepository;
    private final LecItemRepository lecItemRepository;

    @Transactional
    public List<LecQuestionDTO> searchEvaluations(
            Long deptId, String grade, String lectureName, String name) {

        log.info("Search params - deptId: {}, grade: {}, lectureName: {}, name: {}",
                deptId, grade, lectureName, name);

        Long gradeCodeId = getGradeCodeId(grade);  // 학년을 코드 ID로 변환

        return lecQuestionRepository.findEvaluations(
                deptId,
                gradeCodeId,
                lectureName,
                name
        );
    }

    private Long getGradeCodeId(String grade) {
        switch (grade) {
            case "1": return 97L;  // 1학년
            case "2": return 98L;  // 2학년
            case "3": return 99L;  // 3학년
            case "4": return 100L; // 4학년
            default: throw new IllegalArgumentException("Invalid grade: " + grade);
        }
    }

    public List<Map<String, Object>> getAllDepartments() {
        return deptRepository.findAll().stream()
                .map(dept -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("deptId", dept.getDeptId());
                    map.put("deptName", dept.getDeptName());
                    return map;
                })
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getProfessorDepartment() {
        Member professor = authService.getCurrentMember();

        Map<String, Object> deptMap = new HashMap<>();
        deptMap.put("deptId", professor.getDept().getDeptId());
        deptMap.put("deptName", professor.getDept().getDeptName());

        return Collections.singletonList(deptMap);
    }

    // 학생의 수강 강의 목록과 강의평가 여부 조회
    public List<Map<String, Object>> getStudentLecturesWithEvalStatus(Long studentId) {
        log.info("학생 수강 강의 목록 조회 - studentId: {}", studentId);
        return ofregistrationRepository.findLecEvalLecturesWithEvalStatus(studentId);
    }

    // 강의평가 여부 확인
    public boolean isAlreadyEvaluated(Long ofregistrationId) {
        return lecItemRepository.existsByOfRegistration_Id(ofregistrationId);
    }

    // 강의평가 문항 조회
    public List<LecQuestionDTO> getLecQuestions(Long ofregistrationId) {
        log.info("강의평가 문항 조회 - ofregistrationId: {}", ofregistrationId);

        return lecQuestionRepository.findAll().stream()
                .map(question -> LecQuestionDTO.builder()
                        .questionId(question.getQuestionId())
                        .questionName(question.getQuestionName())
                        .build())
                .collect(Collectors.toList());
    }

    // 기존 답변 조회
    public Map<Long, Integer> getPreviousAnswers(Long ofregistrationId) {
        List<LecItem> lecItems = lecItemRepository.findByOfRegistration_Id(ofregistrationId);
        return lecItems.stream()
                .collect(Collectors.toMap(
                        item -> item.getLecQuestion().getQuestionId(),
                        LecItem::getScore
                ));
    }

    // 강의평가 저장
    @Transactional
    public void saveLecEvaluation(Long ofregistrationId, Map<Long, Integer> scores) {
        Ofregistration ofRegistration = ofregistrationRepository.findById(ofregistrationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ofregistrationId: " + ofregistrationId));

        scores.forEach((questionId, score) -> {
            LecItem lecItem = new LecItem();
            lecItem.setOfRegistration(ofRegistration);
            LecQuestion lecQuestion = lecQuestionRepository.findById(questionId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid questionId: " + questionId));

            lecItem.setLecQuestion(lecQuestion);
            lecItem.setScore(score);
            lecItemRepository.save(lecItem);
        });
    }
}