package edu.du.campusflow;

import edu.du.campusflow.entity.LecQuestion;
import edu.du.campusflow.service.LecQuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class DooinTest {

    @Autowired
    private LecQuestionService lecQuestionService;

    @Test
        //@Transactional  // 트랜잭션 제거
    void lecQuestionTest() {
        // 1. 실제 데이터 출력
        System.out.println("=== 강의평가 문항 목록 ===");
        List<LecQuestion> lecQuestions = lecQuestionService.getAllQuestions();
        lecQuestions.forEach(q ->
                System.out.println(q.getQuestionId() + ": " + q.getQuestionName())
        );



        // 2. 검증
        assertFalse(lecQuestions.isEmpty(), "강의평가 문항이 없습니다!");
    }
}