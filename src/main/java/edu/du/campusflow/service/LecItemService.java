package edu.du.campusflow.service;

import edu.du.campusflow.entity.LecItem;
import edu.du.campusflow.repository.LecItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LecItemService {

    private final LecItemRepository lecItemRepository;

    /**
     * 특정 수강신청의 강의평가 답변들 조회
     */
//    public List<LecItem> getLecItemsByOfRegistrationId(Long ofregistrationId) {
//        return lecItemRepository.findByOfRegistration_Id(ofregistrationId);
//    }

    /**
     * 특정 문항에 대한 답변들 조회
     */
    public List<LecItem> getLecItemsByQuestionId(Long questionId) {
        return lecItemRepository.findByLecQuestion_QuestionId(questionId);
    }

    /**
     * 강의평가 제출 여부 확인
     */
//    public boolean isAlreadySubmitted(Long ofregistrationId) {
//        return lecItemRepository.existsByOfRegistration_Id(ofregistrationId);
//    }

    /**
     * 강의평가 답변들 최초 제출
     * 이미 제출된 경우 예외 발생
     */
//    @Transactional
//    public List<LecItem> submitLecItems(List<LecItem> lecItems, Long ofregistrationId) {
//        if (isAlreadySubmitted(ofregistrationId)) {
//            throw new IllegalStateException("이미 제출된 강의평가입니다.");
//        }
//        return lecItemRepository.saveAll(lecItems);
//    }
}