package edu.du.campusflow.dto;

import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.Lecture;
import edu.du.campusflow.entity.Ofregistration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfregistrationDTO {
    // 강의 기본 정보
    private Long lectureId;              // 강의 ID
    private String lectureName;          // 강의명
    private String deptName;             // 학과명
    private String grade;               // 학년
    private String subjectType;          // 이수구분 (전공필수, 교양 등)
    private Integer subjectCredits;             // 학점

    // 교수 정보
    private Long memberId;           // 교수 아이디
    private String name;             // 담당교수(member테이블)

    // 강의 세부 정보
    private String lectureDay;          //강의 요일
    private String startTime;           //시작 시간
    private String endTime;             //종료 시간
    private String facilityName;         // 강의실
    private Integer maxStudents;         // 정원
    private Integer currentStudents;     // 현재 수강인원
    private String dayNight;             // 주야구분

    // 수강신청 상태 정보
    private String regStatus;   // 수강신청 상태
    private Boolean retake;            // 재수강 여부

}