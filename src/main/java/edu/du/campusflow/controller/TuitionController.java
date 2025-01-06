package edu.du.campusflow.controller;

import edu.du.campusflow.dto.TuitionDTO;
import edu.du.campusflow.service.TuitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/iframe/tuition")
@RequiredArgsConstructor
public class TuitionController {

    private final TuitionService tuitionService;

    /**
     * 등록금 대상자 관리 페이지를 보여줍니다.
     */
    @GetMapping("/admin/tuitionTarget")
    public String showTuitionTargetPage(Model model) {
        model.addAttribute("tuitionTargets", tuitionService.getTuitionInfo());
        return "view/iframe/tuition/admin/tuitionTarget";
    }

    /**
     * 등록금 대상자 검색 API
     */
    @GetMapping("/api/search")
    @ResponseBody
    public Page<TuitionDTO> searchTuitionTargets(
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false) String studentName,
            @RequestParam(required = false) String department,
            @RequestParam(defaultValue = "0") int page) {
        return tuitionService.searchTuitionTargets(studentId, studentName, department, page);
    }

    /**
     * 납부 상태 업데이트 API
     */
    @PostMapping("/api/update-status")
    @ResponseBody
    public ResponseEntity<String> updatePaymentStatus(@RequestBody TuitionDTO tuitionDTO) {
        tuitionService.updatePaymentStatus(tuitionDTO.getTargetId(), tuitionDTO.isNewPaymentStatus());
        return ResponseEntity.ok("상태가 업데이트되었습니다.");
    }
}
