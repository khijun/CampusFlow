package edu.du.campusflow.repository;


import edu.du.campusflow.entity.Dept;
import edu.du.campusflow.entity.Tuition;
import edu.du.campusflow.entity.TuitionTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TuitionRepository extends JpaRepository<Tuition, Integer> {
    Tuition findByDeptId(Dept dept);



    // 기본 CRUD 메서드는 JpaRepository가 제공
    // 추가적인 쿼리 메서드를 작성할 수 있음
}
