package edu.du.campusflow.service;

import edu.du.campusflow.dto.CategoryDTO;
import edu.du.campusflow.entity.Category;
import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.Member;
import edu.du.campusflow.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryDTO findById(Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        CategoryDTO result = CategoryDTO.fromEntity(category);
        if(category != null&&categoryRepository.existsByParent(category)) {
            List<CategoryDTO> children = new ArrayList<>();
            for(Category c : categoryRepository.findByParentOrderByOrderNoAsc(category)) {
                children.add(CategoryDTO.fromEntity(c));
            }
            result.setChildren(children);
        }
        return result;
    }

    public List<CategoryDTO> findByType(CommonCode memberType){
        List<Category> rootCategories = categoryRepository.findByParentIsNullAndMemberTypeOrderByOrderNoAsc(memberType);
        rootCategories.forEach(category -> category.setChildren(categoryRepository.findByParentOrderByOrderNoAsc(category)));
        return CategoryDTO.fromEntities(rootCategories);
    }

    public List<CategoryDTO> findByType(Member member){
        return findByType(member.getMemberType());
    }
}
