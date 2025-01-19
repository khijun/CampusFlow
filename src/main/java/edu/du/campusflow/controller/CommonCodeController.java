package edu.du.campusflow.controller;

import edu.du.campusflow.dto.CommonCodeDTO;
import edu.du.campusflow.service.CommonCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/common-codes")
public class CommonCodeController {

    private final CommonCodeService commonCodeService;

    // ✅ 특정 코드 그룹 조회 API (기존 `findByGroupCode` 활용)
    @GetMapping("/{groupCode}")
    public ResponseEntity<List<CommonCodeDTO>> getCommonCodes(@PathVariable String groupCode) {
        List<CommonCodeDTO> response = commonCodeService.getCommonCodesByGroup(groupCode);
        return ResponseEntity.ok(response);
    }
}
