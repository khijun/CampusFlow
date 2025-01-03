package edu.du.campusflow.repository;

import edu.du.campusflow.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByDept_DeptIdAndRelatedPostIsNull(Long deptId);
    // 추가적인 쿼리 메서드가 필요하면 여기에 정의할 수 있습니다.
}