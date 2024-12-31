package edu.du.campusflow.controller;

import edu.du.campusflow.entity.ChangeHistory;
import edu.du.campusflow.service.ChangeHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/change-history")
@RequiredArgsConstructor
public class ChangeHistoryController {

    private final ChangeHistoryService changeHistoryService;

    // 변동 이력 기록 (신청 완료 후)
    @PostMapping("/record")
    public ResponseEntity<ChangeHistory> recordChangeHistory(
            @RequestParam Long studentId,
            @RequestParam Long beforeCodeId,
            @RequestParam Long afterCodeId) {

        ChangeHistory changeHistory = changeHistoryService.recordChangeHistory(studentId, beforeCodeId, afterCodeId);
        return new ResponseEntity<>(changeHistory, HttpStatus.CREATED);
    }

    // 학생의 변동 이력 조회
    @GetMapping("/history/{studentId}")
    public ResponseEntity<Optional<ChangeHistory>> getChangeHistory(@PathVariable Long studentId) {
        Optional<ChangeHistory> changeHistories = changeHistoryService.getChangeHistoryByStudentId(studentId);
        return new ResponseEntity<>(changeHistories, HttpStatus.OK);
    }
}

