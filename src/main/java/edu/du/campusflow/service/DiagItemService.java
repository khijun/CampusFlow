package edu.du.campusflow.service;

import edu.du.campusflow.entity.DiagItem;
import edu.du.campusflow.repository.DiagItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiagItemService {

    private final DiagItemRepository diagItemRepository;

    /**
     * 특정 문항에 대한 답변들 조회
     */
    public List<DiagItem> getDiagItemsByQuestionId(Long questionId) {
        return diagItemRepository.findByDiagQuestion_QuestionId(questionId);
    }

    /**
     * 진단평가 제출 여부 확인
     */
    public boolean isAlreadySubmitted(Long ofregistrationId) {
        return diagItemRepository.existsByOfRegistration_Id(ofregistrationId);
    }
}