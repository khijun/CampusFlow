package edu.du.campusflow.repository;

import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.Dept;
import edu.du.campusflow.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByDept(Dept dept);

    @EntityGraph(attributePaths = {"memberType"})
    Optional<Member> findById(Long id);

    @EntityGraph(attributePaths = {"memberType"})
    List<Member> findAll();

    List<Member> findByAcademicStatus(CommonCode academicStatus); // CommonCode로 Member 찾기

    @Query("select m from Member m where " +
            "(:memberType is null or m.memberType.codeId = :memberType) " +
            "and (:isActive is null or m.isActive = :isActive) " +
            "and (:deptId is null or m.dept.deptId = :deptId) " +
            "and (:name is null or m.name like :name) " +
            "and (:tel is null or m.tel like :tel) " +
            "and (:birthStart is null or m.birthDate > :birthStart) " +
            "and (:birthEnd is null or m.birthDate < :birthEnd) " +
            "and (:createAtStart is null or m.createAt > :createAtStart) " +
            "and (:createAtEnd is null or m.createAt < :createAtEnd) " +
            "and (:academicStatus is null or m.academicStatus.codeId = :academicStatus) " +
            "and (:grade is null or m.grade.codeId = :grade) " +
            "and (:startDateStart is null or m.startDate > :startDateStart) " +
            "and (:startDateEnd is null or m.startDate < :startDateEnd)" +
            "and (:endDateStart is null or m.endDate > :endDateStart) " +
            "and (:endDateEnd is null or m.endDate < :endDateEnd)")

    @EntityGraph(attributePaths = {"dept", "gender", "academicStatus", "grade", "memberType"})
    List<Member> findAllWithFilter(
            @Param("memberType") Long memberType,
            @Param("isActive") Boolean isActive,
            @Param("deptId") Long deptId,
            @Param("name") String name,
            @Param("tel") String tel,
            @Param("birthStart") LocalDate birthStart,
            @Param("birthEnd") LocalDate birthEnd,
            @Param("createAtStart") LocalDateTime createAtStart,
            @Param("createAtEnd") LocalDateTime createAtEnd,
            @Param("academicStatus") Long academicStatus,
            @Param("grade") Long grade,
            @Param("startDateStart") LocalDate startDateStart,
            @Param("startDateEnd") LocalDate startDateEnd,
            @Param("endDateStart") LocalDate endDateStart,
            @Param("endDateEnd") LocalDate endDateEnd
    );


//    @Query("select m from Member m where m.isActive = :isActive")
//    @EntityGraph(attributePaths = {"dept", "gender", "academicStatus", "grade", "memberType"})
//    List<Member> findAllWithDetails(Boolean isActive);

}
