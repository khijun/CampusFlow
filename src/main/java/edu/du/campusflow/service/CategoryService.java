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
        return CategoryDTO.fromEntity(category, findByParent(category));
    }

//    public List<CategoryDTO> findByTypeOld(CommonCode memberType){
//        System.out.println("!!findByType()");
//        List<Category> categories = categoryRepository.findByParentIsNullAndMemberTypeOrderByOrderNoAsc(memberType);
//        List<CategoryDTO> result = new ArrayList<>();
//        for(Category category : categories){
//            result.add(CategoryDTO.fromEntity(category, findByParent(category)));
//        }
//        return result;
//    }

    public List<CategoryDTO> findByType(Member member){
        return findByType(member.getMemberType());
    }

    public List<CategoryDTO> findByParent(Category category){
        List<CategoryDTO> result = new ArrayList<>();
        if(category != null&&categoryRepository.existsByParent(category)) {
            for(Category c : categoryRepository.findByParentOrderByOrderNoAsc(category)) {
                result.add(CategoryDTO.fromEntity(c));
            }
        }
        return result;
    }

    public List<CategoryDTO> findByType(CommonCode memberType){
        System.out.println("!!findByTypeNew()");
        List<Category> categories = categoryRepository.findByParentIsNullAndMemberTypeOrderByOrderNoAsc(memberType);
        List<CategoryDTO> result = new ArrayList<>();
        for(Category category : categories){
            CategoryDTO dto = CategoryDTO.fromEntity(category);
            for(Category child : category.getChildren()){
                dto.addChildren(child);
            }
            result.add(dto);
        }
        return result;
    }
}
