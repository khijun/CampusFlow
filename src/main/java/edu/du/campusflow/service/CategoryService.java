package edu.du.campusflow.service;

import edu.du.campusflow.entity.Category;
import edu.du.campusflow.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
