package edu.du.campusflow.repository;

import edu.du.campusflow.dto.DeptSearchFilter;
import edu.du.campusflow.entity.Dept;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DeptRepository extends JpaRepository<Dept, Long> {
    @EntityGraph(attributePaths = {"deptStatus"})
    List<Dept> findAll();

    @EntityGraph(attributePaths = {"deptStatus"})
    Optional<Dept> findById(Long deptId);

    @Query("select d from Dept d where " +
            "(:#{#filter.deptName} is null or d.deptName like :#{#filter.deptName}) " +
            "and (:#{#filter.minStudentValue} is null or d.maxStudents >= :#{#filter.minStudentValue}) " +
            "and (:#{#filter.maxStudentValue} is null or d.maxStudents <= :#{#filter.maxStudentValue}) " +
            "and (:#{#filter.deptStatus} is null or d.deptStatus.codeId = :#{#filter.deptStatus}) ")
    @EntityGraph(attributePaths = {"deptStatus"})
    List<Dept> findAllWithFilter(@Param("filter") DeptSearchFilter filter);

    @Query("SELECT d FROM Dept d WHERE d.deptId = :deptId")
    List<Dept> findByDeptId(@Param("deptId") Long deptId);

    @Query("SELECT d FROM Dept d WHERE LOWER(d.deptName) LIKE LOWER(CONCAT('%', :deptName, '%'))")
    List<Dept> findByDeptNameContaining(@Param("deptName") String deptName);

    @Query("SELECT d FROM Dept d WHERE d.deptId = :deptId AND LOWER(d.deptName) LIKE LOWER(CONCAT('%', :deptName, '%'))")
    List<Dept> findByDeptIdAndDeptName(@Param("deptId") Long deptId, @Param("deptName") String deptName);
}
