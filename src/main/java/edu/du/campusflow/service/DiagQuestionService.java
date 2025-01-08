package edu.du.campusflow.service;

import edu.du.campusflow.dto.DiagQuestionDTO;
import edu.du.campusflow.entity.*;
import edu.du.campusflow.repository.DiagItemRepository;
import edu.du.campusflow.repository.DiagQuestionRepository;
import edu.du.campusflow.repository.OfregistrationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiagQuestionService {

    private final DiagQuestionRepository diagQuestionRepository;
    private final DiagItemRepository diagItemRepository;
    private final OfregistrationRepository ofregistrationRepository;
    private final AuthService authService;
    private final DiagItemService diagItemService;

    public List<DiagQuestion> getAllQuestions() {
        return diagQuestionRepository.findAll();
    }

    public List<Map<String, Object>> getProfessorLectures() {
        Member currentMember = authService.getCurrentMember();
        log.info("현재 로그인한 교수: {}", currentMember.getName());

        List<Ofregistration> ofregistrations = ofregistrationRepository.findDistinctByLectureId_Member(currentMember);

        return ofregistrations.stream()
                .map(reg -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("ofregistrationId", reg.getId());
                    map.put("lectureName", reg.getLectureId().getLectureName());
                    map.put("semester", reg.getLectureId().getSemester().getCodeName());
                    return map;
                })
                .collect(Collectors.toList());
    }

    public List<DiagQuestionDTO> getDiagnosticResults(Long ofregistrationId) {
        Ofregistration ofregistration = ofregistrationRepository.findById(ofregistrationId)
                .orElseThrow(() -> new IllegalArgumentException("수강신청 정보를 찾을 수 없습니다."));

        // 해당 수강신청의 모든 진단평가 답변 조회
        List<DiagItem> diagItems = diagItemService.getDiagItemsByOfRegistrationId(ofregistrationId);

        // 모든 문항 조회
        List<DiagQuestion> questions = getAllQuestions();
        List<DiagQuestionDTO> results = new ArrayList<>();

        // 각 문항별로 통계 계산
        for (DiagQuestion question : questions) {
            DiagQuestionDTO dto = new DiagQuestionDTO();
            dto.setQuestionId(question.getQuestionId());
            dto.setQuestionName(question.getQuestionName());

            // 강의 정보 설정
            Lecture lecture = ofregistration.getLectureId();
            dto.setLectureName(lecture.getLectureName());
            dto.setName(lecture.getMember().getName());
            dto.setSemester(lecture.getSemester().getCodeName());
            dto.setSubjectId(lecture.getCurriculumSubject().getSubject().getSubjectId());

            // 해당 문항에 대한 답변들만 필터링
            List<DiagItem> itemsForQuestion = diagItems.stream()
                    .filter(item -> item.getDiagQuestion().getQuestionId().equals(question.getQuestionId()))
                    .collect(Collectors.toList());

            // 평균 점수 계산
            double averageScore = itemsForQuestion.stream()
                    .mapToInt(DiagItem::getScore)
                    .average()
                    .orElse(0.0);
            dto.setAverageScore(averageScore);

            // 각 점수별 응답 수와 비율 계산
            int totalResponses = itemsForQuestion.size();

            // 점수별 카운트 설정
            dto.setScore5Count(countScores(itemsForQuestion, 5));
            dto.setScore4Count(countScores(itemsForQuestion, 4));
            dto.setScore3Count(countScores(itemsForQuestion, 3));
            dto.setScore2Count(countScores(itemsForQuestion, 2));
            dto.setScore1Count(countScores(itemsForQuestion, 1));

            // 점수별 비율 설정
            if (totalResponses > 0) {
                dto.setScore5Percent(calculatePercent(dto.getScore5Count(), totalResponses));
                dto.setScore4Percent(calculatePercent(dto.getScore4Count(), totalResponses));
                dto.setScore3Percent(calculatePercent(dto.getScore3Count(), totalResponses));
                dto.setScore2Percent(calculatePercent(dto.getScore2Count(), totalResponses));
                dto.setScore1Percent(calculatePercent(dto.getScore1Count(), totalResponses));
            }

            results.add(dto);
        }

        return results;
    }

    // 특정 점수의 응답 수를 계산하는 헬퍼 메서드
    private long countScores(List<DiagItem> items, int score) {
        return items.stream()
                .filter(item -> item.getScore() == score)
                .count();
    }

    // 백분율을 계산하는 헬퍼 메서드
    private double calculatePercent(long count, int total) {
        return (count * 100.0) / total;
    }

    // admin 검색 기능을 위한 메서드 추가
    public List<DiagQuestionDTO> getDiagnosticResultsBySearchCriteria(
            Long departmentId, Integer grade, String lectureName, String studentName) {

        // 검색 조건에 맞는 수강신청 정보 찾기
        List<Ofregistration> ofregistrations = ofregistrationRepository
                .findBySearchCriteria(departmentId, grade, lectureName, studentName);

        if (ofregistrations.isEmpty()) {
            return new ArrayList<>();
        }

        // 첫 번째 학생의 진단평가 결과 조회
        return getDiagnosticResults(ofregistrations.get(0).getId());
    }
}