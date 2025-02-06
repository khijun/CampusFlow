package edu.du.campusflow.service;

import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.Completion;
import edu.du.campusflow.entity.Grade;
import edu.du.campusflow.entity.Ofregistration;
import edu.du.campusflow.repository.CommonCodeRepository;
import edu.du.campusflow.repository.CompletionRepository;
import edu.du.campusflow.repository.GradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompletionService {
    
    private final CompletionRepository completionRepository;
    private final CommonCodeRepository commonCodeRepository;
    private final GradeRepository gradeRepository;

    @Transactional
    public void createCompletion(Ofregistration ofregistration) {
        // 이미 Completion이 존재하는지 확인
        boolean exists = completionRepository.existsByOfRegistration(ofregistration);
        if (exists) {
            return; // 이미 존재하면 생성하지 않음
        }

        // IN_PROGRESS 상태 코드 조회 (진행 중 상태)
        CommonCode inProgressStatus = commonCodeRepository.findByCodeValue("IN_PROGRESS");
        if (inProgressStatus == null) {
            throw new IllegalArgumentException("진행 중 상태 코드를 찾을 수 없습니다.");
        }

        // Completion 엔티티 생성
        Completion completion = Completion.builder()
                .ofRegistration(ofregistration)
                .completionState(inProgressStatus)
                .createdAt(LocalDateTime.now())
                .build();

        completion = completionRepository.save(completion); // 저장 후 ID 획득

        // 성적 유형 코드 ID (67: 중간, 68: 기말, 69: 과제, 70: 출석)
        List<Long> gradeTypeIds = Arrays.asList(67L, 68L, 69L, 70L);

        // 성적 테이블에 기본값 추가
        for (Long codeId : gradeTypeIds) {
            CommonCode gradeType = commonCodeRepository.findById(codeId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid grade type ID: " + codeId));

            Grade grade = Grade.builder()
                    .completion(completion)
                    .gradeType(gradeType)
                    .score(-1)  // 기본값 null
                    .build();

            gradeRepository.save(grade);
        }
    }
}
