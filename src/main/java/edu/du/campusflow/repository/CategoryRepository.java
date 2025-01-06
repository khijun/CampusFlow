package edu.du.campusflow.repository;

import edu.du.campusflow.entity.Category;
import edu.du.campusflow.entity.CommonCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    public List<Category> findByParentOrderByOrderNoAsc(Category parent);
    public boolean existsByParent(Category parent);
    // 최상위 카테고리를 타입을 입력받아 정렬 순서대로 출력
    public List<Category> findByParentIsNullAndMemberTypeOrderByOrderNoAsc(CommonCode memberType);
}
