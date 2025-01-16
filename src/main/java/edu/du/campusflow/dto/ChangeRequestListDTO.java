package edu.du.campusflow.dto;

import edu.du.campusflow.entity.ChangeRequest;
import lombok.Data;

@Data
public class ChangeRequestListDTO {
    private Long applicationId;  // 신청서 ID를 Long 타입으로 추가
    private Long memberId;  // 멤버 아이디를 Long 타입으로
    private String beforeState;
    private String afterState;
    private String applicationStatus;
    private String reason;

    // 생성자
    public ChangeRequestListDTO(ChangeRequest request) {
        this.applicationId = request.getId();  // 신청서 ID를 추가
        this.memberId = request.getMember() != null ? request.getMember().getMemberId() : null;
        this.beforeState = request.getBeforeCode() != null ? request.getBeforeCode().getCodeName() : "";
        this.afterState = request.getAfterCode() != null ? request.getAfterCode().getCodeName() : "";
        this.applicationStatus = request.getApplicationStatus() != null ? request.getApplicationStatus().getCodeName() : "";
        this.reason = request.getReason();
    }
}
