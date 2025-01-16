package edu.du.campusflow;

import edu.du.campusflow.dto.LecQuestionDTO;
import edu.du.campusflow.entity.LecItem;
import edu.du.campusflow.entity.LecQuestion;
import edu.du.campusflow.repository.LecItemRepository;
import edu.du.campusflow.repository.LecQuestionRepository;
import edu.du.campusflow.repository.OfregistrationRepository;
import edu.du.campusflow.service.LecQuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class DooinTest {

    @Autowired
    private LecQuestionService lecQuestionService;
    @Autowired
    private OfregistrationRepository ofregistrationRepository;
    @Autowired
    private LecQuestionRepository lecQuestionRepository;
    @Autowired
    private LecItemRepository lecItemRepository;

    @Test
    void checkEnvironmentVariables() {
        System.out.println("CF_URL: " + System.getenv("CF_URL"));
        System.out.println("CF_PORT: " + System.getenv("CF_PORT"));
        System.out.println("CF_SCHEMA: " + System.getenv("CF_SCHEMA"));
        System.out.println("CF_USERNAME: " + System.getenv("CF_USERNAME"));
        System.out.println("CF_PASSWORD: " + System.getenv("CF_PASSWORD"));
    }

    @Test
    @Transactional
    void testEvaluationResults() {
        // 1. 강의평가 문항 출력
        List<LecQuestion> questions = lecQuestionService.getAllQuestions();
        System.out.println("\n=== 강의평가 문항 목록 ===");
        questions.forEach(q ->
                System.out.println("문항 ID: " + q.getQuestionId() + ", 문항: " + q.getQuestionName())
        );
        assertFalse(questions.isEmpty(), "강의평가 문항이 없습니다");

        // 2. 강의평가 응답 출력
        List<LecItem> existingItems = lecItemRepository.findAll();
        System.out.println("\n=== 강의평가 응답 목록 ===");
        existingItems.forEach(item -> {
            System.out.println("응답 ID: " + item.getAnswerId());
            System.out.println("수강신청 ID: " + item.getOfRegistration().getId());
            System.out.println("문항 ID: " + item.getLecQuestion().getQuestionId());
            System.out.println("점수: " + item.getScore());
            System.out.println("------------------------");
        });

        if (!existingItems.isEmpty()) {
            // 3. 결과 조회 및 출력
            Long ofregistrationId = existingItems.get(0).getOfRegistration().getId();
            System.out.println("\n=== 선택된 수강신청 ID: " + ofregistrationId + " ===");

            List<LecQuestionDTO> results = lecQuestionService.getEvaluationResults(ofregistrationId);
            assertFalse(results.isEmpty(), "강의평가 결과가 없습니다");

            System.out.println("\n=== 강의평가 결과 ===");
            results.forEach(result -> {
                System.out.println("문항: " + result.getQuestionName());
                System.out.println("평균 점수: " + result.getAverageScore());
                System.out.println("5점 응답 수: " + result.getScore5Count());
                System.out.println("------------------------");
            });
        } else {
            System.out.println("기존 강의평가 응답이 없습니다. 데이터를 먼저 입력해주세요.");
        }
    }
}