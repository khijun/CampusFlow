package edu.du.campusflow.service;

import edu.du.campusflow.entity.DiagQuestion;
import edu.du.campusflow.repository.DiagQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiagQuestionService {

    private final DiagQuestionRepository diagQuestionRepository;

    /**
     * 모든 진단평가 문항 조회
     */
    public List<DiagQuestion> getAllQuestions() {
        return diagQuestionRepository.findAll();
    }

    /**
     * ID로 진단평가 문항 조회
     */
    public DiagQuestion getQuestionById(Long id) {
        return diagQuestionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("진단평가 문항을 찾을 수 없습니다. ID: " + id));
    }

    /**
     * 문항 이름으로 검색
     */
    public List<DiagQuestion> searchQuestionsByName(String keyword) {
        return diagQuestionRepository.findByQuestionNameContaining(keyword);
    }

    /**
     * 진단평가 문항 저장 (신규 등록)
     */
    @Transactional
    public DiagQuestion createQuestion(DiagQuestion question) {
        return diagQuestionRepository.save(question);
    }

    /**
     * 진단평가 문항 수정
     */
    @Transactional
    public DiagQuestion updateQuestion(Long id, DiagQuestion questionDetails) {
        DiagQuestion question = getQuestionById(id);
        question.setQuestionName(questionDetails.getQuestionName());
        return diagQuestionRepository.save(question);
    }

    /**
     * 진단평가 문항 삭제
     */
    @Transactional
    public void deleteQuestion(Long id) {
        // 해당 문항에 대한 답변이 있는지 확인하는 로직 추가 필요
        diagQuestionRepository.deleteById(id);
    }
}