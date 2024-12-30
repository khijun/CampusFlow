package edu.du.campusflow.service;

import edu.du.campusflow.entity.ChangeHistory;
import edu.du.campusflow.entity.ChangeRequest;
import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.Student;
import edu.du.campusflow.enums.AcademicStatus;
import edu.du.campusflow.repository.ChangeHistoryRepository;
import edu.du.campusflow.repository.ChangeRequestRepository;
import edu.du.campusflow.repository.CommonCodeRepository;
import edu.du.campusflow.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChangeService {

    private final ChangeRequestRepository changeRequestRepository;
    private final ChangeHistoryRepository changeHistoryRepository;
    private final CommonCodeRepository commonCodeRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public void processChangeRequest(Long requestId) {
        // 1. 변동 신청 조회
        ChangeRequest changeRequest = changeRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("변동 신청을 찾을 수 없습니다."));

        // 2. 변경 전후 학적 상태 확인
        CommonCode beforeCode = changeRequest.getBeforeCode();
        CommonCode afterCode = changeRequest.getAfterCode();

        // 3. Enum과 매칭된 상태 확인
        if (!isValidTransition(beforeCode, afterCode)) {
            throw new IllegalArgumentException("허용되지 않는 학적 상태 변환입니다.");
        }

        // 4. 변동 이력 저장
        ChangeHistory changeHistory = ChangeHistory.builder()
                .student(changeRequest.getStudent())
                .beforeCode(beforeCode)
                .afterCode(afterCode)
                .approvalDate(LocalDateTime.now())
                .grade(changeRequest.getStudent().getGrade())
                .build();
        changeHistoryRepository.save(changeHistory);

        // 5. 신청 상태 업데이트 (처리 완료)
        changeRequest.setApplicationStatus(afterCode);
        changeRequestRepository.save(changeRequest);
    }

    @Transactional
    public Long createChangeRequest(Long studentId, String beforeCodeValue, String afterCodeValue) {
        // 학생 정보 조회
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("학생을 찾을 수 없습니다."));

        // 변경 전 코드와 변경 후 코드 조회
        CommonCode beforeCode = commonCodeRepository.findByCodeValue(beforeCodeValue);
        CommonCode afterCode = commonCodeRepository.findByCodeValue(afterCodeValue);

        // ChangeRequest 엔티티 생성
        ChangeRequest changeRequest = ChangeRequest.builder()
                .student(student)
                .beforeCode(beforeCode)
                .afterCode(afterCode)
                .requestDate(LocalDateTime.now())
                .academicStatus(beforeCode) // 초기 학적 상태는 변경 전 코드로 설정
                .applicationStatus(beforeCode) // 초기 신청 상태는 변경 전 코드로 설정
                .build();

        // 변동 신청을 DB에 저장
        changeRequest = changeRequestRepository.save(changeRequest);

        // 생성된 신청 ID 반환
        return changeRequest.getId();
    }

    private boolean isValidTransition(CommonCode beforeCode, CommonCode afterCode) {
        // AcademicStatus Enum과 비교
        AcademicStatus beforeStatus = AcademicStatus.valueOf(beforeCode.getCodeValue());
        AcademicStatus afterStatus = AcademicStatus.valueOf(afterCode.getCodeValue());

        // 허용되는 상태 변경 조건 정의
        if (beforeStatus == AcademicStatus.LEAVE_OF_ABSENCE && afterStatus == AcademicStatus.RETURNED) {
            return true;
        }
        if (beforeStatus == AcademicStatus.ENROLLED && afterStatus == AcademicStatus.GRADUATED) {
            return true;
        }
        // 기타 상태 전환 로직 추가
        return false;
    }
}
