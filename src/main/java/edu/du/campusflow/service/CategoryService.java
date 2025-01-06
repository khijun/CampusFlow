package edu.du.campusflow.service;

import edu.du.campusflow.entity.Category;
import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category findById(Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if(category == null||!categoryRepository.existsByParent(category)) return category;

        category.setChildren(categoryRepository.findByParentOrderByOrderNoAsc(category));
        return category;
    }

    public List<Category> findByType(CommonCode memberType){
        List<Category> rootCategories = categoryRepository.findByParentIsNullAndMemberTypeOrderByOrderNoAsc(memberType);
        rootCategories.forEach(category -> category.setChildren(categoryRepository.findByParentOrderByOrderNoAsc(category)));
        return rootCategories;
    }
}
