package edu.du.campusflow.controller;

import edu.du.campusflow.dto.TuitionDTO;
import edu.du.campusflow.entity.Member;
import edu.du.campusflow.entity.TuitionTarget;
import edu.du.campusflow.repository.MemberRepository;
import edu.du.campusflow.service.TuitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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

    /**
     * 등록금 대상자 관리 페이지를 보여줍니다.
     */
    @GetMapping("/admin/tuitionTarget")
    public String showTuitionTargetPage(Model model, Member student) {
        List<TuitionDTO> tuitionDTO = tuitionService.findAllTuitionDTO();
        model.addAttribute("tuitionTarget", tuitionDTO);  // 등록금 대상자 정보
        return "view/iframe/tuition/admin/tuitionTarget";  // Thymeleaf 템플릿 이름 // "tuitionTargets"라는 이름으로 데이터 추가

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




}
