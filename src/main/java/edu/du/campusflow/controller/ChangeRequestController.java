package edu.du.campusflow.controller;

import edu.du.campusflow.entity.ChangeRequest;
import edu.du.campusflow.service.ChangeRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/change-requests")
@RequiredArgsConstructor
public class ChangeRequestController {

    private final ChangeRequestService changeRequestService;

    // 변동 신청 생성 (신청서 제출)
    @PostMapping("/submit")
    public ResponseEntity<ChangeRequest> submitChangeRequest(
            @RequestParam Long studentId,
            @RequestParam Long beforeCodeId,
            @RequestParam Long afterCodeId,
            @RequestParam Long academicStatusId,
            @RequestParam Long applicationStatusId) {

        ChangeRequest changeRequest = changeRequestService.submitChangeRequest(
                studentId, beforeCodeId, afterCodeId, academicStatusId, applicationStatusId
        );
        return new ResponseEntity<>(changeRequest, HttpStatus.CREATED);
    }

    // 신청 승인 처리
    @PatchMapping("/approve/{requestId}")
    public ResponseEntity<ChangeRequest> approveChangeRequest(@PathVariable Long requestId) {
        ChangeRequest approvedRequest = changeRequestService.approveChangeRequest(requestId);
        return new ResponseEntity<>(approvedRequest, HttpStatus.OK);
    }

    // 신청 거절 처리
    @PatchMapping("/deny/{requestId}")
    public ResponseEntity<ChangeRequest> denyChangeRequest(@PathVariable Long requestId) {
        ChangeRequest deniedRequest = changeRequestService.denyChangeRequest(requestId);
        return new ResponseEntity<>(deniedRequest, HttpStatus.OK);
    }

    // 대기 중 상태 처리
    @PatchMapping("/set-pending/{requestId}")
    public ResponseEntity<ChangeRequest> setPendingChangeRequest(@PathVariable Long requestId) {
        ChangeRequest pendingRequest = changeRequestService.setPendingChangeRequest(requestId);
        return new ResponseEntity<>(pendingRequest, HttpStatus.OK);
    }

    // 신청 중 상태 처리
    @PatchMapping("/set-application/{requestId}")
    public ResponseEntity<ChangeRequest> setApplicationChangeRequest(@PathVariable Long requestId) {
        ChangeRequest applicationRequest = changeRequestService.setApplicationChangeRequest(requestId);
        return new ResponseEntity<>(applicationRequest, HttpStatus.OK);
    }

    // 신청서 목록 조회
    @GetMapping("/list")
    public ResponseEntity<List<ChangeRequest>> getChangeRequestList() {
        List<ChangeRequest> changeRequests = changeRequestService.getAllChangeRequests();
        return new ResponseEntity<>(changeRequests, HttpStatus.OK);
    }
}
