package edu.du.campusflow.dto;

import edu.du.campusflow.entity.Category;
import edu.du.campusflow.entity.FileInfo;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CategoryDTO {

    private Long id;
    private String name;
    private FileInfo image; // image 정보를 그대로 보내기 원할 경우. 필요시 이미지 URL 등으로 변환 가능
    private String url;
    private Boolean isActive;
    private Integer orderNo;
    private List<CategoryDTO> children; // children도 DTO로 변환하여 전달

    // 엔티티를 DTO로 변환하는 메서드 (필요한 경우)
    public static CategoryDTO fromEntity(Category category, List<CategoryDTO> children) {
        if(category == null) return null;
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .image(category.getImage())
                .url(category.getUrl())
                .isActive(category.getIsActive())
                .orderNo(category.getOrderNo())
                .children(children)
                .build();
    }

    // 자식 요소를 넣지 않을 시 빈 리스트를 삽입
    public static CategoryDTO fromEntity(Category category) {
        return fromEntity(category, new ArrayList<>());
    }
}