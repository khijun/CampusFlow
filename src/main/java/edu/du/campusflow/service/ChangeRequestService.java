package edu.du.campusflow.service;

import edu.du.campusflow.entity.ChangeRequest;
import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.Student;
import edu.du.campusflow.repository.ChangeRequestRepository;
import edu.du.campusflow.repository.CommonCodeRepository;
import edu.du.campusflow.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChangeRequestService {


    private final ChangeRequestRepository changeRequestRepository;


    private final CommonCodeRepository commonCodeRepository;


    private final StudentRepository studentRepository;

    // 신청 처리
    public ChangeRequest submitChangeRequest(Long studentId, Long beforeCodeId, Long afterCodeId, Long academicStatusId, Long applicationStatusId) {
        // 학생 확인
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("학생이 존재하지 않습니다."));

        // CommonCode에서 변경 전/후 코드, 학적 상태, 신청 상태 조회
        CommonCode beforeCode = commonCodeRepository.findByCodeId(beforeCodeId)
                .orElseThrow(() -> new RuntimeException("변경 전 코드가 존재하지 않습니다."));
        CommonCode afterCode = commonCodeRepository.findByCodeId(afterCodeId)
                .orElseThrow(() -> new RuntimeException("변경 후 코드가 존재하지 않습니다."));
        CommonCode academicStatus = commonCodeRepository.findByCodeId(academicStatusId)
                .orElseThrow(() -> new RuntimeException("학적 상태 코드가 존재하지 않습니다."));
        CommonCode applicationStatus = commonCodeRepository.findByCodeId(applicationStatusId)
                .orElseThrow(() -> new RuntimeException("신청 상태 코드가 존재하지 않습니다."));

        // 신청 데이터 생성
        ChangeRequest changeRequest = ChangeRequest.builder()
                .student(student)
                .beforeCode(beforeCode)
                .afterCode(afterCode)
                .academicStatus(academicStatus)
                .applicationStatus(applicationStatus)
                .requestDate(LocalDateTime.now())
                .build();

        // 신청 데이터 저장
        return changeRequestRepository.save(changeRequest);
    }
    // 승인 처리
    public ChangeRequest approveChangeRequest(Long requestId) {
        ChangeRequest request = changeRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("신청서가 존재하지 않습니다."));

        // 신청 상태를 승인 상태로 변경
        CommonCode acceptedStatus = commonCodeRepository.findByCodeId(12L)  // 1L은 예시로, 실제로는 '승인' 상태 코드의 ID를 넣어야 함
                .orElseThrow(() -> new RuntimeException("승인 상태 코드가 존재하지 않습니다."));

        request.setApplicationStatus(acceptedStatus);
        request.setRequestDate(LocalDateTime.now());

        // 신청서 저장
        return changeRequestRepository.save(request);
    }

    // 거절 처리
    public ChangeRequest denyChangeRequest(Long requestId) {
        ChangeRequest request = changeRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("신청서가 존재하지 않습니다."));

        // 신청 상태를 거절 상태로 변경
        CommonCode deniedStatus = commonCodeRepository.findByCodeId(13L)  // 2L은 예시로, 실제로는 '거절' 상태 코드의 ID를 넣어야 함
                .orElseThrow(() -> new RuntimeException("거절 상태 코드가 존재하지 않습니다."));

        request.setApplicationStatus(deniedStatus);
        request.setRequestDate(LocalDateTime.now());

        // 신청서 저장
        return changeRequestRepository.save(request);
    }
    // 대기 중 처리
    public ChangeRequest setPendingChangeRequest(Long requestId) {
        ChangeRequest request = changeRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("신청서가 존재하지 않습니다."));

        // 신청 상태를 대기중으로 변경
        CommonCode waitingStatus = commonCodeRepository.findByCodeId(14L)  // 2L은 '대기중' 상태 코드 ID
                .orElseThrow(() -> new RuntimeException("대기중 상태 코드가 존재하지 않습니다."));

        request.setApplicationStatus(waitingStatus);
        request.setRequestDate(LocalDateTime.now());

        // 신청서 저장
        return changeRequestRepository.save(request);
    }

    // 신청 중 처리
    public ChangeRequest setApplicationChangeRequest(Long requestId) {
        ChangeRequest request = changeRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("신청서가 존재하지 않습니다."));

        // 신청 상태를 신청중으로 변경
        CommonCode applicationStatus = commonCodeRepository.findByCodeId(15L)  // 3L은 '신청중' 상태 코드 ID
                .orElseThrow(() -> new RuntimeException("신청중 상태 코드가 존재하지 않습니다."));

        request.setApplicationStatus(applicationStatus);
        request.setRequestDate(LocalDateTime.now());

        // 신청서 저장
        return changeRequestRepository.save(request);
    }

    // 모든 신청 목록 조회
    public List<ChangeRequest> getAllChangeRequests() {
        return changeRequestRepository.findAll();
    }
}
