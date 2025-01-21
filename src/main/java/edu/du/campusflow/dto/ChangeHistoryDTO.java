package edu.du.campusflow.dto;

import edu.du.campusflow.entity.ChangeHistory;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangeHistoryDTO {
    private Long memberId;
    private String name;
    private String grade;
    private String beforeState;
    private Long deptId; // 학과 필드 추가
    private String deptName;
    private String afterState;
    private String approvalDate;

    // ChangeHistory 객체를 인자로 받는 생성자
    public ChangeHistoryDTO(ChangeHistory history) {
        // ChangeHistory 객체에서 필요한 값들을 추출하여 DTO에 세팅
        this.memberId = history.getMember() != null ? history.getMember().getMemberId() : null;
        this.name = history.getMember() != null ? history.getMember().getName() : null;
        this.grade = history.getGrade() != null ? history.getGrade().getCodeName() : null;
        this.beforeState = history.getBeforeCode() != null ? history.getBeforeCode().getCodeName() : null;
        this.afterState = history.getAfterCode() != null ? history.getAfterCode().getCodeName() : null;
        this.approvalDate = history.getApprovalDate() != null ? history.getApprovalDate().toString() : null;
        this.deptId = history.getMember() != null ? history.getMember().getDept().getDeptId() : null;
        this.deptName = history.getMember() != null ? history.getMember().getDept().getDeptName() : null;
    }
}
