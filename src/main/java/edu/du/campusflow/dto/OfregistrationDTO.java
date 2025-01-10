package edu.du.campusflow.dto;

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
    private String subjectName;          // 교과목명
    private Integer grade;               // 학년
    private String subjectType;          // 이수구분 (전공필수, 교양 등)
    private Integer credits;             // 학점

    // 교수 정보
    private  String  memberId;           // 교수 아이디
    private String professorName;        // 담당교수

    // 강의 세부 정보
    private String lectureTime;          // 강의시간
    private String facilityName;         // 강의실
    private Integer maxStudents;         // 정원
    private Integer currentStudents;     // 현재 수강인원
    private String dayNight;             // 주야구분
    private Long syllabusFileId;         // 강의계획서 파일 ID

    // 수강신청 상태 정보
    private String registrationStatus;   // 수강신청 상태
    private Boolean isRetake;            // 재수강 여부

}