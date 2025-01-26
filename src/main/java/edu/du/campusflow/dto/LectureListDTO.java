package edu.du.campusflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LectureListDTO {
    private Long lectureId;     // 강의 아이디
    private String lectureName; // 강의명
}
