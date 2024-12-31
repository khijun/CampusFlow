package edu.du.campusflow.service;

import edu.du.campusflow.entity.DiagFeedback;
import edu.du.campusflow.entity.DiagItem;
import edu.du.campusflow.repository.DiagFeedbackRepository;
import edu.du.campusflow.repository.DiagItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// feature/evaluation ㅁㄴㅇㅁㄴㅇㅁㅁㄴㅇ

@Service
@RequiredArgsConstructor
public class DiagFeedbackService {

    private final DiagFeedbackRepository diagFeedbackRepository;
    private final DiagItemRepository diagItemRepository;

    /**
     * 특정 수강신청에 대한 진단평가 답변들 조회
     */
    public List<DiagItem> getDiagItems(Long ofregistrationId) {
        return diagItemRepository.findByOfRegistration_Id(ofregistrationId);
    }

    /**
     * 특정 수강신청에 대한 피드백 조회
     */
    public DiagFeedback getFeedbackByOfregistrationId(Long ofregistrationId) {
        return diagFeedbackRepository.findByOfRegistration_Id(ofregistrationId);
    }

    /**
     * 피드백 저장 (신규 작성 또는 수정)
     */
    @Transactional
    public DiagFeedback saveFeedback(DiagFeedback feedback) {
        return diagFeedbackRepository.save(feedback);
    }

    /**
     * 피드백 삭제
     */
    @Transactional
    public void deleteFeedback(Long id) {
        diagFeedbackRepository.deleteById(id);
    }
}