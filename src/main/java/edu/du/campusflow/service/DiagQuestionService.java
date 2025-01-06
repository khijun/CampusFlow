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

        Lecture lecture = ofregistration.getLectureId();
        List<DiagQuestion> questions = getAllQuestions();
        List<DiagQuestionDTO> results = new ArrayList<>();

        for (DiagQuestion question : questions) {
            List<DiagItem> items = diagItemRepository.findByOfRegistration_IdAndDiagQuestion_QuestionId(
                    ofregistrationId,
                    question.getQuestionId()
            );

            DiagQuestionDTO result = new DiagQuestionDTO();
            result.setQuestionId(question.getQuestionId());
            result.setQuestionName(question.getQuestionName());
            result.setLectureName(lecture.getLectureName());
            result.setName(lecture.getMember().getName());
            result.setSemester(lecture.getSemester().getCodeName());
            result.setSubjectId(lecture.getCurriculumSubject().getSubject().getSubjectId());

            if (!items.isEmpty()) {
                calculateScores(items, result);
            }

            results.add(result);
        }

        return results;
    }

    private void calculateScores(List<DiagItem> items, DiagQuestionDTO result) {
        double avgScore = items.stream()
                .mapToInt(DiagItem::getScore)
                .average()
                .orElse(0.0);
        result.setAverageScore(avgScore);

        Map<Integer, Long> scoreCounts = items.stream()
                .collect(Collectors.groupingBy(
                        DiagItem::getScore,
                        Collectors.counting()
                ));

        result.setScore5Count(scoreCounts.getOrDefault(5, 0L));
        result.setScore4Count(scoreCounts.getOrDefault(4, 0L));
        result.setScore3Count(scoreCounts.getOrDefault(3, 0L));
        result.setScore2Count(scoreCounts.getOrDefault(2, 0L));
        result.setScore1Count(scoreCounts.getOrDefault(1, 0L));

        long total = items.size();
        if (total > 0) {
            result.setScore5Percent((double) result.getScore5Count() / total * 100);
            result.setScore4Percent((double) result.getScore4Count() / total * 100);
            result.setScore3Percent((double) result.getScore3Count() / total * 100);
            result.setScore2Percent((double) result.getScore2Count() / total * 100);
            result.setScore1Percent((double) result.getScore1Count() / total * 100);
        }
    }
}