package edu.du.campusflow.dto;

import edu.du.campusflow.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class SimpleMemberDTO {
    private Long memberId;
    private Long memberType;
    private String name;
    private String academicStatus;
    private Long deptId;
    private String deptName;

    public SimpleMemberDTO(Member member) {
        this.memberId = member.getMemberId();
        this.memberType = member.getMemberType().getCodeId();
        this.name = member.getName();
        this.academicStatus = member.getAcademicStatus().getCodeName();
        this.deptId = member.getDept().getDeptId();
        this.deptName = member.getDept().getDeptName();
    }



}
