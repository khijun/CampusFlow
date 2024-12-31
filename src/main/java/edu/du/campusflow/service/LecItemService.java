package edu.du.campusflow.service;

import edu.du.campusflow.entity.LecItem;
import edu.du.campusflow.entity.LecQuestion;
import edu.du.campusflow.entity.Ofregistration;
import edu.du.campusflow.repository.LecItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LecItemService {

    private final LecItemRepository lecItemRepository;
    private final LecQuestionService lecQuestionService;

    /**
     * 특정 수강신청의 강의평가 답변들 조회
     */
    public List<LecItem> getLecItemsByOfRegistrationId(Long ofregistrationId) {
        return lecItemRepository.findByOfRegistration_Id(ofregistrationId);
    }

    /**
     * 특정 문항에 대한 답변들 조회
     */
    public List<LecItem> getLecItemsByQuestionId(Long questionId) {
        return lecItemRepository.findByLecQuestion_QuestionId(questionId);
    }

    /**
     * 강의평가 제출 여부 확인
     */
    public boolean isAlreadySubmitted(Long ofregistrationId) {
        return lecItemRepository.existsByOfRegistration_Id(ofregistrationId);
    }

    /**
     * Map 형태의 점수 데이터를 LecItem 리스트로 변환하여 제출
     */
    @Transactional
    public List<LecItem> submitEvaluation(Long ofregistrationId, Map<String, Integer> scores) {
        // 1. 이미 제출된 평가인지 확인
        if (isAlreadySubmitted(ofregistrationId)) {
            throw new IllegalStateException("이미 제출된 강의평가입니다.");
        }

        // 2. LecItem 리스트 생성
        List<LecItem> items = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            String key = entry.getKey();
            Integer score = entry.getValue();

            // scores[1] 형태의 키에서 questionId 추출
            Long questionId = Long.parseLong(key.substring(key.indexOf('[') + 1, key.indexOf(']')));

            LecItem item = new LecItem();
            item.setScore(score);

            // 수강신청 정보 설정
            Ofregistration ofregistration = new Ofregistration();
            ofregistration.setId(ofregistrationId);
            item.setOfRegistration(ofregistration);

            // 문항 정보 설정
            LecQuestion question = lecQuestionService.getQuestionById(questionId);
            item.setLecQuestion(question);

            items.add(item);
        }

        // 3. 강의평가 답변들 저장
        return lecItemRepository.saveAll(items);
    }
}