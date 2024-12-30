package edu.du.campusflow.service;

import edu.du.campusflow.entity.LecQuestion;
import edu.du.campusflow.repository.LecQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LecQuestionService {

    private final LecQuestionRepository lecQuestionRepository;

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
}