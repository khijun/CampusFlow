package edu.du.campusflow.controller;

import edu.du.campusflow.dto.ChangeRequestListDTO;
import edu.du.campusflow.dto.ChangeHistoryDTO;
import edu.du.campusflow.dto.SimpleMemberDTO;
import edu.du.campusflow.entity.ChangeHistory;
import edu.du.campusflow.entity.ChangeRequest;
import edu.du.campusflow.entity.Member;
import edu.du.campusflow.service.AuthService;
import edu.du.campusflow.service.ChangeHistoryService;
import edu.du.campusflow.service.ChangeRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AcademicController {
    private final AuthService authService;
    private final ChangeRequestService changeRequestService;
    private final ChangeHistoryService changeHistoryService;

   @GetMapping("/api/academic-change-request")
    public ResponseEntity<List<ChangeRequest>> getChangeRequests() {
       Long memberId = authService.getCurrentMemberId();
       List<ChangeRequest> changeRequests = changeRequestService.getChangeRequestsByMemberId(memberId);
       return ResponseEntity.ok(changeRequests);
   }

    @GetMapping("/api/admin/change-request-history")
    @PreAuthorize("hasAnyRole('STAFF')")
    public ResponseEntity<List<ChangeHistoryDTO>> getChangeRequestHistory() {
        List<ChangeHistory> changeHistories = changeHistoryService.findAll();
        List<ChangeHistoryDTO> changeHistoryDTOs = changeHistories.stream()
                .map(ChangeHistoryDTO::new) // ChangeHistory -> ChangeHistoryDTO 변환
                .collect(Collectors.toList());
        return ResponseEntity.ok(changeHistoryDTOs);
    }

    @GetMapping("/api/admin/academic-change-request")
    @PreAuthorize("hasAnyRole('STAFF')")
    public ResponseEntity<List<ChangeRequestListDTO>> getChangeRequest() {
        List<ChangeRequest> changeRequests = changeRequestService.getALlChangeRequests();
        List<ChangeRequestListDTO> changeRequestListDTOs = changeRequests.stream()
                .map(ChangeRequestListDTO :: new) // ChangeRequest -> ChangeRequestListDTO 변환
                .collect(Collectors.toList());
        return ResponseEntity.ok(changeRequestListDTOs);
    }

    @GetMapping("/api/admin/academic-management")
    @PreAuthorize("hasAnyRole('STAFF')")
    public ResponseEntity<List<SimpleMemberDTO>> getAcademicManagement() {
       List<SimpleMemberDTO> members = changeHistoryService.getMembersByType();
       return ResponseEntity.ok(members);
    }
}
