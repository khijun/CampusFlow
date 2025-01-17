package edu.du.campusflow.dto;

import lombok.Data;

@Data
public class FacilityDTO {

    private Long facilityId;          //강의실 코드
    private String facilityName;      //강의실 이름
    private String building;      //건물명
    private String floor;         //층수
    private Integer capacity;         //강의실 수용인원
    private String facilityStatus;    //강의실 상태
}
