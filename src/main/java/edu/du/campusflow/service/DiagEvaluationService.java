package edu.du.campusflow.service;
import edu.du.campusflow.dto.DiagEvaluationDetailDTO;
import edu.du.campusflow.dto.DiagQuestionDTO;
import edu.du.campusflow.entity.*;
import edu.du.campusflow.repository.DiagQuestionRepository;
import edu.du.campusflow.repository.OfregistrationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiagEvaluationService {
    private final OfregistrationRepository ofregistrationRepository;
    private final AuthService authService;
    private final DiagItemService diagItemService;
    private final DiagQuestionRepository diagQuestionRepository;

    @Transactional
    public List<Map<String, Object>> getProfessorLectures() {
        Member currentMember = authService.getCurrentMember();
        log.info("현재 로그인한 교수: {}", currentMember.getName());

        List<Ofregistration> ofregistrations = ofregistrationRepository.findDistinctByLectureId_Member(currentMember);
        return convertToLectureMap(ofregistrations);
    }

    @Transactional
    public List<Map<String, Object>> getAllLectures() {
        List<Ofregistration> ofregistrations = ofregistrationRepository.findAll();
        return convertToLectureMap(ofregistrations);
    }

    @Transactional
    public List<DiagQuestionDTO> getDiagnosticResults(Long ofregistrationId) {
        Ofregistration ofregistration = ofregistrationRepository.findById(ofregistrationId)
                .orElseThrow(() -> new IllegalArgumentException("수강신청 정보를 찾을 수 없습니다."));

        List<DiagItem> diagItems = diagItemService.getDiagItemsByOfRegistrationId(ofregistrationId);

        if (diagItems.isEmpty()) {
            List<DiagQuestion> questions = diagQuestionRepository.findAll();
            return createEmptyResults(questions, ofregistration);
        }

        return convertToDiagQuestionDTO(diagItems, ofregistration);
    }

    @Transactional
    public List<DiagQuestionDTO> searchDiagnosticResults(
            Long deptId,
            String grade,
            String lectureName,
            String studentName
    ) {
        List<DiagEvaluationDetailDTO> evaluations = ofregistrationRepository
                .findEvaluationsBySearchCriteria(deptId, grade, lectureName, studentName);  // 여기를 변경

        if (evaluations.isEmpty()) {
            return new ArrayList<>();
        }

        Map<String, DiagQuestionDTO> resultMap = new HashMap<>();

        evaluations.forEach(eval -> {
            String key = eval.getLectureName() + "_" + eval.getQuestionName();
            DiagQuestionDTO dto = resultMap.computeIfAbsent(key, k -> {
                DiagQuestionDTO newDto = new DiagQuestionDTO();
                newDto.setLectureName(eval.getLectureName());
                newDto.setQuestionName(eval.getQuestionName());
                newDto.initializeScoreCounts();
                return newDto;
            });

            dto.incrementScoreCount(eval.getScore());
        });

        resultMap.values().forEach(dto -> {
            int totalResponses = dto.calculateTotalResponses();
            if (totalResponses > 0) {
                dto.calculatePercentages(totalResponses);
            }
        });

        return new ArrayList<>(resultMap.values());
    }

    private List<Map<String, Object>> convertToLectureMap(List<Ofregistration> ofregistrations) {
        return ofregistrations.stream()
                .map(reg -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("ofregistrationId", reg.getId());
                    map.put("lectureName", reg.getLectureId().getLectureName());
                    map.put("professorName", reg.getLectureId().getMember().getName());
                    map.put("semester", reg.getLectureId().getSemester().getCodeName());
                    return map;
                })
                .collect(Collectors.toList());
    }

    private List<DiagQuestionDTO> createEmptyResults(List<DiagQuestion> questions, Ofregistration ofregistration) {
        List<DiagQuestionDTO> results = new ArrayList<>();
        Lecture lecture = ofregistration.getLectureId();

        for (DiagQuestion question : questions) {
            DiagQuestionDTO dto = new DiagQuestionDTO();
            dto.setQuestionId(question.getQuestionId());
            dto.setQuestionName(question.getQuestionName());
            dto.setLectureName(lecture.getLectureName());
            dto.setName(lecture.getMember().getName());
            dto.setSemester(lecture.getSemester().getCodeName());
            dto.setSubjectId(lecture.getCurriculumSubject().getSubject().getSubjectId());
            dto.setAverageScore(0.0);
            dto.initializeScoreCounts();
            results.add(dto);
        }

        return results;
    }

    private List<DiagQuestionDTO> convertToDiagQuestionDTO(List<DiagItem> diagItems, Ofregistration ofregistration) {
        Map<Long, List<DiagItem>> itemsByQuestionId = diagItems.stream()
                .collect(Collectors.groupingBy(item -> item.getDiagQuestion().getQuestionId()));

        List<DiagQuestionDTO> results = new ArrayList<>();

        itemsByQuestionId.forEach((questionId, items) -> {
            DiagQuestionDTO dto = new DiagQuestionDTO();

            // 문항 정보 설정
            DiagQuestion question = items.get(0).getDiagQuestion();
            dto.setQuestionId(questionId);
            dto.setQuestionName(question.getQuestionName());

            // 강의 정보 설정
            Lecture lecture = ofregistration.getLectureId();
            dto.setLectureName(lecture.getLectureName());
            dto.setName(lecture.getMember().getName());
            dto.setSemester(lecture.getSemester().getCodeName());
            dto.setSubjectId(lecture.getCurriculumSubject().getSubject().getSubjectId());

            // 점수별 응답 수 계산
            long score5Count = items.stream().filter(item -> item.getScore() == 5).count();
            long score4Count = items.stream().filter(item -> item.getScore() == 4).count();
            long score3Count = items.stream().filter(item -> item.getScore() == 3).count();
            long score2Count = items.stream().filter(item -> item.getScore() == 2).count();
            long score1Count = items.stream().filter(item -> item.getScore() == 1).count();

            dto.setScore5Count((int) score5Count);
            dto.setScore4Count((int) score4Count);
            dto.setScore3Count((int) score3Count);
            dto.setScore2Count((int) score2Count);
            dto.setScore1Count((int) score1Count);

            // 평균 점수 계산
            double totalScore = items.stream()
                    .mapToInt(DiagItem::getScore)
                    .sum();
            dto.setAverageScore(totalScore / items.size());

            // 백분율 계산
            int totalResponses = items.size();
            dto.setScore5Percent((double) score5Count / totalResponses * 100);
            dto.setScore4Percent((double) score4Count / totalResponses * 100);
            dto.setScore3Percent((double) score3Count / totalResponses * 100);
            dto.setScore2Percent((double) score2Count / totalResponses * 100);
            dto.setScore1Percent((double) score1Count / totalResponses * 100);

            results.add(dto);
        });

        return results;
    }
}