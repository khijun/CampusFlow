package edu.du.campusflow.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class GradeUpdateDTO {
    private List<Long> memberIds;
    private List<Long> lectureIds;
    private List<Map<String, Integer>> scores;

}
