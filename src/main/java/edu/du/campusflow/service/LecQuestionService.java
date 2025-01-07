package edu.du.campusflow.service;

import edu.du.campusflow.dto.LecQuestionDTO;
import edu.du.campusflow.entity.*;
import edu.du.campusflow.repository.LecItemRepository;
import edu.du.campusflow.repository.LecQuestionRepository;
import edu.du.campusflow.repository.OfregistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LecQuestionService {

    private final LecQuestionRepository lecQuestionRepository;
    private final LecItemRepository lecItemRepository;
    private final OfregistrationRepository ofregistrationRepository;
    private final AuthService authService;

    public List<Map<String, Object>> getProfessorLectures() {
        // 현재 로그인한 교수의 ID 가져오기
        Member currentMember = authService.getCurrentMember();

        // 해당 교수의 강의 목록 조회
        List<Ofregistration> ofregistrations = ofregistrationRepository.findDistinctByLectureId_Member(currentMember);

        // DTO 변환
        return ofregistrations.stream()
                .map(reg -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("ofregistrationId", reg.getId());
                    map.put("lectureName", reg.getLectureId().getLectureName());
                    map.put("professorName", reg.getLectureId().getMember().getName());
                    map.put("semester", reg.getLectureId().getSemester().getCodeName());  // CommonCode의 codeName 사용
                    map.put("subjectId", reg.getLectureId().getCurriculumSubject().getSubject().getSubjectId());
                    return map;
                })
                .collect(Collectors.toList());
    }

    /**
     * 모든 강의평가 문항 조회
     */
    public List<LecQuestion> getAllQuestions() {
        return lecQuestionRepository.findAll();
    }

    /**
     * ID로 강의평가 문항 조회
     */
    public LecQuestion getQuestionById(Long id) {
        return lecQuestionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("강의평가 문항을 찾을 수 없습니다. ID: " + id));
    }

    public List<Map<String, Object>> getProfessorLectures(Long professorId) {
        // 교수의 강의 목록 조회
        List<Ofregistration> registrations = ofregistrationRepository.findByLectureId_Member_MemberId(professorId);

        // 강의 정보를 Map으로 변환
        return registrations.stream()
                .map(reg -> {
                    Map<String, Object> lecture = new HashMap<>();
                    lecture.put("ofregistrationId", reg.getId());
                    lecture.put("lectureName", reg.getLectureId().getLectureName());
                    lecture.put("professorName", reg.getLectureId().getMember().getName());
                    lecture.put("semester", reg.getLectureId().getSemester().getCodeName());
                    lecture.put("subjectId", reg.getLectureId().getCurriculumSubject().getSubject().getSubjectId());
                    return lecture;
                })
                .collect(Collectors.toList());
    }

    public List<LecQuestionDTO> getEvaluationResults(Long ofregistrationId) {
        // 수강신청 정보 조회
        Ofregistration ofregistration = ofregistrationRepository.findById(ofregistrationId)
                .orElseThrow(() -> new IllegalArgumentException("수강신청 정보를 찾을 수 없습니다."));

        // 강의 정보 가져오기
        Lecture lecture = ofregistration.getLectureId();

        List<LecQuestion> questions = getAllQuestions();
        List<LecQuestionDTO> results = new ArrayList<>();

        for (LecQuestion question : questions) {
            List<LecItem> items = lecItemRepository.findByOfRegistration_IdAndLecQuestion_QuestionId(
                    ofregistrationId,
                    question.getQuestionId()
            );

            LecQuestionDTO result = new LecQuestionDTO();

            // 기본 문항 정보 설정
            result.setQuestionId(question.getQuestionId());
            result.setQuestionName(question.getQuestionName());

            // 강의 정보 설정
            result.setLectureName(lecture.getLectureName());
            result.setName(lecture.getMember().getName());
            result.setSemester(lecture.getSemester().getCodeName());  // CommonCode의 codeName을 가져옴
            result.setSubjectId(lecture.getCurriculumSubject().getSubject().getSubjectId());

            if (!items.isEmpty()) {
                // 평균 점수 계산
                double avgScore = items.stream()
                        .mapToInt(LecItem::getScore)
                        .average()
                        .orElse(0.0);
                result.setAverageScore(avgScore);

                // 점수별 응답 수 계산
                Map<Integer, Long> scoreCounts = items.stream()
                        .collect(Collectors.groupingBy(
                                LecItem::getScore,
                                Collectors.counting()
                        ));

                // 각 점수별 응답 수 설정
                result.setScore5Count(scoreCounts.getOrDefault(5, 0L));
                result.setScore4Count(scoreCounts.getOrDefault(4, 0L));
                result.setScore3Count(scoreCounts.getOrDefault(3, 0L));
                result.setScore2Count(scoreCounts.getOrDefault(2, 0L));
                result.setScore1Count(scoreCounts.getOrDefault(1, 0L));

                // 비율 계산
                long total = items.size();
                if (total > 0) {
                    result.setScore5Percent((double) result.getScore5Count() / total * 100);
                    result.setScore4Percent((double) result.getScore4Count() / total * 100);
                    result.setScore3Percent((double) result.getScore3Count() / total * 100);
                    result.setScore2Percent((double) result.getScore2Count() / total * 100);
                    result.setScore1Percent((double) result.getScore1Count() / total * 100);
                }
            }

            results.add(result);
        }

        return results;
    }
}