package edu.du.campusflow.repository;

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
            ":deptName is null or d.deptName like :deptName " +
            "and :minStudentValue is null or d.maxStudents > :minStudentValue " +
            "and :maxStudentValue is null or d.maxStudents < :maxStudentValue " +
            "and :deptStatus is null or d.deptStatus.codeId = :deptStatus"
    )
    List<Dept> findAllWithFilter(@Param("deptName") String deptName,
                                 @Param("minStudentValue") Integer minStudentValue,
                                 @Param("maxStudentValue") Integer maxStudentValue,
                                 @Param("deptStatus") Long deptStatus);
}
