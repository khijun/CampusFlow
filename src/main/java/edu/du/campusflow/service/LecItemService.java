package edu.du.campusflow.service;

import edu.du.campusflow.entity.LecItem;
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
    private final OfregistrationService ofregistrationService;

    public List<LecItem> getLecItemsByOfRegistrationId(Long ofregistrationId) {
        return lecItemRepository.findByOfRegistration_Id(ofregistrationId);
    }

    public List<LecItem> getLecItemsByQuestionId(Long questionId) {
        return lecItemRepository.findByLecQuestion_QuestionId(questionId);
    }

    public boolean isAlreadySubmitted(Long ofregistrationId) {
        return lecItemRepository.existsByOfRegistration_Id(ofregistrationId);
    }

    @Transactional
    public List<LecItem> submitEvaluation(Long ofregistrationId, Map<String, Integer> scores) {
        if (isAlreadySubmitted(ofregistrationId)) {
            throw new IllegalStateException("이미 제출된 강의평가입니다.");
        }

        Ofregistration ofregistration = ofregistrationService.getOfregistrationById(ofregistrationId);
        if (ofregistration == null) {
            throw new IllegalArgumentException("존재하지 않는 수강신청입니다.");
        }

        List<LecItem> items = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            String key = entry.getKey();
            Integer score = entry.getValue();

            Long questionId = Long.parseLong(key.substring(key.indexOf('[') + 1, key.indexOf(']')));

            LecItem item = new LecItem();
            item.setScore(score);
            item.setOfRegistration(ofregistration);
            item.setLecQuestion(lecQuestionService.getQuestionById(questionId));

            items.add(item);
        }

        return lecItemRepository.saveAll(items);
    }
}