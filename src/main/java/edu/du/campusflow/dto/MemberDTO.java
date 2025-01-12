package edu.du.campusflow.dto;

import edu.du.campusflow.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {
    private Long memberId;
    private String deptName;
    private String name;
    private String tel;
    private String address;
    private LocalDate birthDate;
    private Boolean isActive;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String email;
    private Long fileInfo;
    private String genderStr; // CommonCode 테이블과 연관
    private String academicStatusStr; // CommonCode 테이블과 연관
    private String gradeStr; // CommonCode 테이블과 연관
    private String memberTypeStr; // CommonCode 테이블과 연관
    private LocalDate startDate; // 입학, 임용 날짜
    private LocalDate endDate; // 졸업, 퇴직 날짜

    public static List<MemberDTO> fromEntityList(List<Member> memberList) {
        return memberList.stream().map(MemberDTO::fromEntity).collect(Collectors.toList());
    }

    public static MemberDTO fromEntity(Member member) {
        if(member == null) return null;
        Long fileInfoId = (member.getFileInfo() != null) ? member.getFileInfo().getId() : null;
        String genderStr = (member.getGender() != null) ? member.getGender().getCodeName() : null;
        String academicStatusStr = (member.getAcademicStatus() != null) ? member.getAcademicStatus().getCodeName() : null;
        String gradeStr = (member.getGrade() != null) ? member.getGrade().getCodeName() : null;
        String memberTypeStr = (member.getMemberType() != null) ? member.getMemberType().getCodeName() : null;

        return MemberDTO.builder()
                .memberId(member.getMemberId())
                .deptName(member.getDept().getDeptName()) // dept 이름만 갖고오기
                .name(member.getName())
                .tel(member.getTel())
                .address(member.getAddress())
                .birthDate(member.getBirthDate())
                .isActive(member.getIsActive())
                .createAt(member.getCreateAt())
                .updateAt(member.getUpdateAt())
                .email(member.getEmail())
                .fileInfo(fileInfoId)  // FileInfo를 Long으로 변환
                .genderStr(genderStr) // CommonCode에서 코드 값을 가져옴
                .academicStatusStr(academicStatusStr)
                .gradeStr(gradeStr)
                .memberTypeStr(memberTypeStr)
                .startDate(member.getStartDate())
                .endDate(member.getEndDate())
                .build();
    }
}
