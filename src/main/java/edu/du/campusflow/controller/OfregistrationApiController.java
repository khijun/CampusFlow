package edu.du.campusflow.controller;

import edu.du.campusflow.dto.OfregistrationDTO;
import edu.du.campusflow.entity.Member;
import edu.du.campusflow.service.AuthService;
import edu.du.campusflow.service.OfregistrationService;
import edu.du.campusflow.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/ofregistration")
@RequiredArgsConstructor
public class OfregistrationApiController {

    private final OfregistrationService ofregistrationService;
    private final AuthService authService;
    private final MemberRepository memberRepository;

    /**
     * 수강 가능한 강의 목록 조회
     */
    @GetMapping("/available-lectures")
    public ResponseEntity<List<OfregistrationDTO>> getAvailableLectures() {
        try {
            // 현재 로그인한 학생의 정보 가져오기
            Member currentStudent = memberRepository.findById(authService.getCurrentMemberId())
                .orElseThrow(() -> new IllegalStateException("로그인 정보를 찾을 수 없습니다."));
            
            // 학생의 학과명으로 수강 가능한 강의 목록 조회
            List<OfregistrationDTO> lectures = ofregistrationService.getAllAvailableLectures(
                currentStudent.getDept().getDeptName()
            );
            
            return ResponseEntity.ok(lectures);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 내 수강신청 목록 조회
     */
    @GetMapping("/my-registrations")
    public ResponseEntity<List<OfregistrationDTO>> getMyRegistrations() {
        try {
            Long memberId = authService.getCurrentMemberId();
            List<OfregistrationDTO> registrations = ofregistrationService.getStudentRegistrations(memberId);
            return ResponseEntity.ok(registrations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 관리자용 수강신청 대기 목록 조회
     */
    @GetMapping("/admin/pending")
    public ResponseEntity<List<OfregistrationDTO>> getPendingRegistrations() {
        try {
            List<OfregistrationDTO> registrations = ofregistrationService.getPendingRegistrations();
            return ResponseEntity.ok(registrations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 수강신청 처리
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerLecture(@RequestBody OfregistrationDTO ofregistrationDTO) {
        try {
            // 현재 로그인한 사용자의 ID 가져오기
            Long memberId = authService.getCurrentMemberId();
            if (memberId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("로그인이 필요합니다.");
            }

            // DTO에 회원 ID 설정
            ofregistrationDTO.setMemberId(memberId);

            // 수강신청 처리
            ofregistrationService.registerLecture(ofregistrationDTO);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 관리자용 수강신청 상태 업데이트
     */
    @PostMapping("/admin/update-status")
    public ResponseEntity<?> updateRegistrationStatus(@RequestBody OfregistrationDTO ofregistrationDTO) {
        try {
            // 현재 로그인한 사용자가 관리자인지 확인
            Long adminId = authService.getCurrentMemberId();
            if (adminId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("로그인이 필요합니다.");
            }

            // 수강신청 상태 업데이트
            ofregistrationService.updateRegistrationStatus(
                ofregistrationDTO.getLectureId(),
                ofregistrationDTO.getMemberId(),
                ofregistrationDTO.getRegStatus()
            );

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
} 