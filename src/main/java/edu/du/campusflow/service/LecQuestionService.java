package edu.du.campusflow.service;

import edu.du.campusflow.dto.LecQuestionDTO;
import edu.du.campusflow.entity.LecItem;
import edu.du.campusflow.entity.LecQuestion;
import edu.du.campusflow.repository.LecItemRepository;
import edu.du.campusflow.repository.LecQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LecQuestionService {

    private final LecQuestionRepository lecQuestionRepository;
    private final LecItemRepository lecItemRepository;

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


    public List<LecQuestionDTO> getEvaluationResults(Long ofregistrationId) {
        List<LecQuestion> questions = getAllQuestions();
        List<LecQuestionDTO> results = new ArrayList<>();

        for (LecQuestion question : questions) {
            // 특정 수강신청의 특정 문항에 대한 답변들만 조회
            List<LecItem> items = lecItemRepository.findByOfRegistration_IdAndLecQuestion_QuestionId(
                    ofregistrationId,
                    question.getQuestionId()
            );

            LecQuestionDTO result = new LecQuestionDTO();
            result.setQuestionId(question.getQuestionId());
            result.setQuestionName(question.getQuestionName());

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