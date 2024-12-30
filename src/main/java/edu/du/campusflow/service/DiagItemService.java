package edu.du.campusflow.service;

import edu.du.campusflow.entity.DiagItem;
import edu.du.campusflow.repository.DiagItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiagItemService {

    private final DiagItemRepository diagItemRepository;

    /**
     * 특정 수강신청의 진단평가 답변들 조회
     */
//    public List<DiagItem> getDiagItemsByOfRegistrationId(Long ofregistrationId) {
//        return diagItemRepository.findByOfRegistration_Id(ofregistrationId);
//    } // ofregistration_id

    /**
     * 특정 문항에 대한 답변들 조회
     */
    public List<DiagItem> getDiagItemsByQuestionId(Long questionId) {
        return diagItemRepository.findByDiagQuestion_QuestionId(questionId);
    } // question_Id

    /**
     * 진단평가 제출 여부 확인
     */
//    public boolean isAlreadySubmitted(Long ofregistrationId) {
//        return diagItemRepository.existsByOfRegistration_Id(ofregistrationId);
//    }

    /**
     * 진단평가 답변들 최초 제출
     * 이미 제출된 경우 예외 발생
     */

//    @Transactional
//    public List<DiagItem> submitDiagItems(List<DiagItem> diagItems, Long ofregistrationId) {
//        if (isAlreadySubmitted(ofregistrationId)) {
//            throw new IllegalStateException("이미 제출된 진단평가입니다.");
//        }
//        return diagItemRepository.saveAll(diagItems);
//    }
}