package edu.du.campusflow.service;

import edu.du.campusflow.entity.ChangeHistory;
import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.Student;
import edu.du.campusflow.repository.ChangeHistoryRepository;
import edu.du.campusflow.repository.CommonCodeRepository;
import edu.du.campusflow.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChangeHistoryService {


    private final ChangeHistoryRepository changeHistoryRepository;


    private final CommonCodeRepository commonCodeRepository;


    private final StudentRepository studentRepository;

    // 이력 기록 처리
    public ChangeHistory recordChangeHistory(Long studentId, Long beforeCodeId, Long afterCodeId) {
        // 학생 확인
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("학생이 존재하지 않습니다."));

        // CommonCode에서 변경 전/후 코드 조회
        CommonCode beforeCode = commonCodeRepository.findByCodeId(beforeCodeId)
                .orElseThrow(() -> new RuntimeException("변경 전 코드가 존재하지 않습니다."));
        CommonCode afterCode = commonCodeRepository.findByCodeId(afterCodeId)
                .orElseThrow(() -> new RuntimeException("변경 후 코드가 존재하지 않습니다."));

        // 이력 데이터 생성
        ChangeHistory changeHistory = ChangeHistory.builder()
                .student(student)
                .beforeCode(beforeCode)
                .afterCode(afterCode)
                .approvalDate(LocalDateTime.now())
                .grade(student.getGrade())
                .build();

        // 이력 데이터 저장
        return changeHistoryRepository.save(changeHistory);
    }

    // 학생의 변동 히스토리 조회
    public Optional<ChangeHistory> getChangeHistoryByStudentId(Long studentId) {
        return changeHistoryRepository.findByStudentId(studentId);
    }
}
