package edu.du.campusflow.controller;

import edu.du.campusflow.dto.TuitionDTO;
import edu.du.campusflow.entity.Member;
import edu.du.campusflow.repository.MemberRepository;
import edu.du.campusflow.service.TuitionService;
import edu.du.campusflow.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tuition")
@RequiredArgsConstructor
public class TuitionApiController {

    private final TuitionService tuitionService;
    private final MemberRepository memberRepository;
    private final AuthService authService;

    /**
     * 관리자용 등록금 대상자 목록 조회
     */
    @GetMapping("/admin/targets")
    @PreAuthorize("hasAnyRole('STAFF')")
    public ResponseEntity<List<TuitionDTO>> getTuitionTargets() {
        try {
            List<TuitionDTO> tuitionDTO = tuitionService.findAllTuitionDTO();
            return ResponseEntity.ok(tuitionDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 학생용 등록금 정보 조회
     */
    @GetMapping("/student/info")
    public ResponseEntity<TuitionDTO> getStudentTuition() {
        try {
            Member member = authService.getCurrentMember();
            TuitionDTO tuitionDTO = tuitionService.getTuitionInfo(member);
            return ResponseEntity.ok(tuitionDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 관리자용 납부 상태 업데이트
     */
    @PostMapping("/admin/update-status")
    @PreAuthorize("hasAnyRole('STAFF')")
    public ResponseEntity<?> updatePaymentStatus(@RequestBody TuitionDTO tuitionDTO) {
        try {
            Member member = memberRepository.findById(tuitionDTO.getMemberId())
                    .orElseThrow(() -> new RuntimeException("해당 회원을 찾을 수 없습니다."));
            tuitionService.updatePaymentStatus(member, tuitionDTO.isPaymentStatus());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 관리자용 납부 금액 업데이트
     */
    @PostMapping("/admin/update-paid-amount")
    @PreAuthorize("hasAnyRole('STAFF')")
    public ResponseEntity<?> updatePaidAmount(@RequestBody TuitionDTO tuitionDTO) {
        try {
            Member member = memberRepository.findById(tuitionDTO.getMemberId())
                    .orElseThrow(() -> new RuntimeException("해당 회원을 찾을 수 없습니다."));
            tuitionService.updatePaidAmount(member, tuitionDTO.getPaidAmount());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
} 