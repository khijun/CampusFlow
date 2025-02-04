package edu.du.campusflow.service;

import edu.du.campusflow.entity.DiagFeedback;
import edu.du.campusflow.entity.DiagItem;
import edu.du.campusflow.repository.DiagFeedbackRepository;
import edu.du.campusflow.repository.DiagItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// feature/evaluation.css ㅁㄴㅇㅁㄴㅇㅁㅁㄴㅇ

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
}