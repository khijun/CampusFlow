package edu.du.campusflow.repository;

import edu.du.campusflow.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    public List<Category> findByParentOrderByOrderNoAsc(Category parent);
    public boolean existsByParent(Category parent);
}
