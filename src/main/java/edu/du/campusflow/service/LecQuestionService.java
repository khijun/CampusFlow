package edu.du.campusflow.service;

import edu.du.campusflow.dto.LecQuestionDTO;
import edu.du.campusflow.entity.LecItem;
import edu.du.campusflow.entity.LecQuestion;
import edu.du.campusflow.entity.Member;
import edu.du.campusflow.entity.Ofregistration;
import edu.du.campusflow.repository.LecItemRepository;
import edu.du.campusflow.repository.LecQuestionRepository;
import edu.du.campusflow.repository.OfregistrationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LecQuestionService {

    private final LecQuestionRepository lecQuestionRepository;
    private final LecItemRepository lecItemRepository;
    private final OfregistrationRepository ofregistrationRepository;
    private final AuthService authService;

    public List<Map<String, Object>> getProfessorLectures() {
        Member currentMember = authService.getCurrentMember();
        log.info("현재 로그인한 교수: {}", currentMember.getName());
        return getLecturesByProfessor(currentMember);
    }

    // professorId로 조회하는 메서드
    public List<Map<String, Object>> getProfessorLecturesById(Long professorId) {
        List<Ofregistration> ofregistrations = ofregistrationRepository
                .findByLectureId_Member_MemberId(professorId);
        return convertToLectureMap(ofregistrations);
    }

    private List<Map<String, Object>> getLecturesByProfessor(Member professor) {
        List<Ofregistration> ofregistrations = ofregistrationRepository
                .findDistinctByLectureId_Member(professor);
        return convertToLectureMap(ofregistrations);
    }

    private List<Map<String, Object>> convertToLectureMap(List<Ofregistration> ofregistrations) {
        return ofregistrations.stream()
                .map(reg -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("ofregistrationId", reg.getId());
                    map.put("lectureName", reg.getLectureId().getLectureName());
                    map.put("professorName", reg.getLectureId().getMember().getName());
                    map.put("semester", reg.getLectureId().getSemester().getCodeName());
                    map.put("subjectId", reg.getLectureId().getCurriculumSubject().getSubject().getSubjectId());
                    return map;
                })
                .collect(Collectors.toList());
    }

    public List<LecQuestion> getAllQuestions() {
        return lecQuestionRepository.findAll();
    }

    public LecQuestion getQuestionById(Long id) {
        return lecQuestionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("강의평가 문항을 찾을 수 없습니다. ID: " + id));
    }

    public List<LecQuestionDTO> getEvaluationResults(Long lectureId) {
        // 강의 정보 조회
        List<Ofregistration> allRegistrations = ofregistrationRepository
                .findByLectureId_LectureId(lectureId);

        if (allRegistrations.isEmpty()) {
            throw new IllegalArgumentException("해당 강의의 수강신청 정보를 찾을 수 없습니다.");
        }

        Ofregistration representativeReg = allRegistrations.get(0);
        List<Long> allOfregistrationIds = allRegistrations.stream()
                .map(Ofregistration::getId)
                .collect(Collectors.toList());

        List<LecQuestion> questions = getAllQuestions();
        List<LecQuestionDTO> results = new ArrayList<>();

        for (LecQuestion question : questions) {
            List<LecItem> items = lecItemRepository.findByOfRegistration_IdInAndLecQuestion_QuestionId(
                    allOfregistrationIds,
                    question.getQuestionId()
            );

            LecQuestionDTO result = createQuestionDTO(question, representativeReg, items);
            results.add(result);
        }
        return results;
    }

    private LecQuestionDTO createQuestionDTO(LecQuestion question, Ofregistration registration, List<LecItem> items) {
        LecQuestionDTO result = new LecQuestionDTO();
        result.setQuestionId(question.getQuestionId());
        result.setQuestionName(question.getQuestionName());
        result.setLectureName(registration.getLectureId().getLectureName());
        result.setName(registration.getLectureId().getMember().getName());
        result.setSemester(registration.getLectureId().getSemester().getCodeName());
        result.setSubjectId(registration.getLectureId().getCurriculumSubject().getSubject().getSubjectId());

        if (!items.isEmpty()) {
            calculateScores(result, items);
        }
        return result;
    }

    private void calculateScores(LecQuestionDTO result, List<LecItem> items) {
        double avgScore = items.stream()
                .mapToInt(LecItem::getScore)
                .average()
                .orElse(0.0);
        result.setAverageScore(avgScore);

        Map<Integer, Long> scoreCounts = items.stream()
                .collect(Collectors.groupingBy(
                        LecItem::getScore,
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

    public List<Map<String, Object>> getAllLectures() {
        return ofregistrationRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        reg -> reg.getLectureId().getLectureId(),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    Ofregistration reg = list.get(0);
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("ofregistrationId", reg.getLectureId().getLectureId());
                                    map.put("lectureName", reg.getLectureId().getLectureName());
                                    map.put("semester", reg.getLectureId().getSemester().getCodeName());
                                    return map;
                                }
                        )
                ))
                .values()
                .stream()
                .collect(Collectors.toList());
    }
}