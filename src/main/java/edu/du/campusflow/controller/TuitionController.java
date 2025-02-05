package edu.du.campusflow.controller;

import edu.du.campusflow.dto.TuitionDTO;
import edu.du.campusflow.entity.Member;
import edu.du.campusflow.repository.MemberRepository;
import edu.du.campusflow.service.TuitionService;
import edu.du.campusflow.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/iframe/tuition")
@RequiredArgsConstructor
public class TuitionController {

    private final TuitionService tuitionService;
    private final MemberRepository memberRepository;
    private final AuthService authService;

    /**
     * 등록금 대상자 관리 페이지를 보여줍니다.
     */
    @GetMapping("/admin/tuitionTarget")
    @PreAuthorize("hasAnyRole('STAFF')")
    public String showTuitionTargetPage() {
        return "view/iframe/tuition/admin/tuitionTarget";
    }

    /**
     * 학생 등록금 조회 페이지를 보여줍니다.
     */
    @GetMapping("/student/studentTuition")
    public String showStudentTuitionPage() {
        return "view/iframe/tuition/student/studentTuition";
    }

    /// 납부 상태 변경 API
    @PostMapping("/update-status")
    @ResponseBody
    public void updatePaymentStatus(@RequestBody TuitionDTO tuitionDTO) {
        // memberId를 사용해 Member 객체를 조회
        Member member = memberRepository.findById(tuitionDTO.getMemberId())
                .orElseThrow(() -> new RuntimeException("해당 회원을 찾을 수 없습니다."));

        // 조회한 Member 객체를 사용해 납부 상태 업데이트
        tuitionService.updatePaymentStatus(member, tuitionDTO.isPaymentStatus());
    }

    @PostMapping("/update-paid-amount")
    @ResponseBody
    public void updatePaidAmount(@RequestBody TuitionDTO tuitionDTO) {
        Member member = memberRepository.findById(tuitionDTO.getMemberId())
                .orElseThrow(() -> new RuntimeException("해당 회원을 찾을 수 없습니다."));

        tuitionService.updatePaidAmount(member, tuitionDTO.getPaidAmount());
    }

    @GetMapping("/api/tuition")
    public ResponseEntity<?> getTuition() {
        return ResponseEntity.ok(tuitionService);
    }
}
