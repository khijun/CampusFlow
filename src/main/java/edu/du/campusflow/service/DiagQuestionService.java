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

    public List<DiagQuestionDTO> getDiagnosticResultsBySearchCriteria(
            Long departmentId,
            String grade,           // 학년 파라미터 추가
            String lectureName,
            String studentName) {

        log.info("검색 조건에 따른 진단평가 결과 조회 - 학과 ID: {}, 학년: {}, 강의명: {}, 학생명: {}",
                departmentId, grade, lectureName, studentName);

        // 검색 조건에 학년 추가하여 조회
        List<Ofregistration> ofregistrations = ofregistrationRepository.findBySearchCriteria(
                departmentId,
                grade,
                lectureName,
                studentName
        );
        log.info("검색된 수강신청 수: {}", ofregistrations.size());

        if (ofregistrations.isEmpty()) {
            return new ArrayList<>();
        }

        List<DiagItem> allDiagItems = getAllDiagItems(ofregistrations);
        log.info("조회된 진단평가 항목 수: {}", allDiagItems.size());

        // 대표 수강신청 정보 설정
        Ofregistration representativeOfregistration = ofregistrations.get(0);

        // 학생 이름으로 검색한 경우 해당 학생의 결과만 처리
        if (studentName != null && !studentName.trim().isEmpty()) {
            List<DiagItem> studentDiagItems = filterDiagItemsByStudent(
                    allDiagItems,
                    representativeOfregistration.getMember().getMemberId()
            );
            return processQuestionResults(studentDiagItems, representativeOfregistration);
        }

        // 학과/학년 기준 검색인 경우 전체 통계 처리
        return processQuestionResults(allDiagItems, representativeOfregistration);
    }

    public List<Map<String, Object>> getProfessorLectures() {
        Member currentMember = authService.getCurrentMember();
        List<Ofregistration> ofregistrations = ofregistrationRepository.findDistinctByLectureId_Member(currentMember);
        return ofregistrations.stream()
                .map(this::convertToLectureMap)
                .collect(Collectors.toList());
    }

    public List<DiagQuestionDTO> getDiagnosticResults(Long ofregistrationId) {
        Ofregistration ofregistration = ofregistrationRepository.findById(ofregistrationId)
                .orElseThrow(() -> new IllegalArgumentException("수강신청 정보를 찾을 수 없습니다."));
        List<DiagItem> diagItems = diagItemService.getDiagItemsByOfRegistrationId(ofregistrationId);
        return processQuestionResults(diagItems, ofregistration);
    }

    /**
     * 특정 학생의 진단평가 항목만 필터링
     */
    private List<DiagItem> filterDiagItemsByStudent(List<DiagItem> diagItems, Long memberId) {
        return diagItems.stream()
                .filter(item -> item.getOfRegistration().getMember().getMemberId().equals(memberId))
                .collect(Collectors.toList());
    }

    private List<DiagItem> getAllDiagItems(List<Ofregistration> ofregistrations) {
        List<DiagItem> allDiagItems = new ArrayList<>();
        for (Ofregistration ofregistration : ofregistrations) {
            allDiagItems.addAll(diagItemRepository.findByOfRegistration_Id(ofregistration.getId()));
        }
        return allDiagItems;
    }

    private List<DiagQuestionDTO> processQuestionResults(List<DiagItem> diagItems, Ofregistration ofregistration) {
        List<DiagQuestion> questions = getAllQuestions();
        return questions.stream()
                .map(question -> createQuestionDTO(question, diagItems, ofregistration))
                .collect(Collectors.toList());
    }

    private DiagQuestionDTO createQuestionDTO(DiagQuestion question, List<DiagItem> diagItems, Ofregistration ofregistration) {
        DiagQuestionDTO dto = new DiagQuestionDTO();
        dto.setQuestionId(question.getQuestionId());
        dto.setQuestionName(question.getQuestionName());

        if (ofregistration != null) {
            setLectureInfo(dto, ofregistration.getLectureId());
        }

        List<DiagItem> itemsForQuestion = filterItemsByQuestion(diagItems, question);
        if (!itemsForQuestion.isEmpty()) {
            calculateScoresAndPercentages(dto, itemsForQuestion);
        }

        return dto;
    }

    private void calculateScoresAndPercentages(DiagQuestionDTO dto, List<DiagItem> items) {
        dto.setAverageScore(items.stream().mapToInt(DiagItem::getScore).average().orElse(0.0));
        int totalResponses = items.size();

        for (int score = 5; score >= 1; score--) {
            long count = countScores(items, score);
            setScoreCount(dto, score, count);
            if (totalResponses > 0) {
                setScorePercent(dto, score, calculatePercent(count, totalResponses));
            }
        }
    }

    private List<DiagItem> filterItemsByQuestion(List<DiagItem> diagItems, DiagQuestion question) {
        return diagItems.stream()
                .filter(item -> item.getDiagQuestion().getQuestionId().equals(question.getQuestionId()))
                .collect(Collectors.toList());
    }

    private Map<String, Object> convertToLectureMap(Ofregistration reg) {
        Map<String, Object> map = new HashMap<>();
        map.put("ofregistrationId", reg.getId());
        map.put("lectureName", reg.getLectureId().getLectureName());
        map.put("semester", reg.getLectureId().getSemester().getCodeName());
        return map;
    }

    private void setLectureInfo(DiagQuestionDTO dto, Lecture lecture) {
        dto.setLectureName(lecture.getLectureName());
        dto.setName(lecture.getMember().getName());
        dto.setSemester(lecture.getSemester().getCodeName());
        dto.setSubjectId(lecture.getCurriculumSubject().getSubject().getSubjectId());
    }

    private void setScoreCount(DiagQuestionDTO dto, int score, long count) {
        switch (score) {
            case 5: dto.setScore5Count(count); break;
            case 4: dto.setScore4Count(count); break;
            case 3: dto.setScore3Count(count); break;
            case 2: dto.setScore2Count(count); break;
            case 1: dto.setScore1Count(count); break;
        }
    }

    private void setScorePercent(DiagQuestionDTO dto, int score, double percent) {
        switch (score) {
            case 5: dto.setScore5Percent(percent); break;
            case 4: dto.setScore4Percent(percent); break;
            case 3: dto.setScore3Percent(percent); break;
            case 2: dto.setScore2Percent(percent); break;
            case 1: dto.setScore1Percent(percent); break;
        }
    }

    private long countScores(List<DiagItem> items, int score) {
        return items.stream()
                .filter(item -> item.getScore() == score)
                .count();
    }

    private double calculatePercent(long count, int total) {
        return (count * 100.0) / total;
    }
}