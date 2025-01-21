package edu.du.campusflow.repository;

import edu.du.campusflow.dto.MemberSearchFilter;
import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.Dept;
import edu.du.campusflow.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByDept(Dept dept);

    @EntityGraph(attributePaths = {"memberType", "dept", "gender", "academicStatus", "grade"})
    Optional<Member> findById(Long id);

    @EntityGraph(attributePaths = {"memberType", "dept"})
    List<Member> findAll();

    List<Member> findByAcademicStatus(CommonCode academicStatus); // CommonCode로 Member 찾기

    @Query("SELECT m FROM Member m WHERE " +
            "(:#{#filter.memberType} IS NULL OR m.memberType.codeId = :#{#filter.memberType}) " +
            "AND (:#{#filter.isActive} IS NULL OR m.isActive = :#{#filter.isActive}) " +
            "AND (:#{#filter.deptId} IS NULL OR m.dept.deptId = :#{#filter.deptId}) " +
            "AND (:#{#filter.name} IS NULL OR m.name LIKE %:#{#filter.name}%) " +
            "AND (:#{#filter.tel} IS NULL OR m.tel LIKE %:#{#filter.tel}%) " +
            "AND (:#{#filter.birthStart} IS NULL OR m.birthDate >= :#{#filter.birthStart}) " +
            "AND (:#{#filter.birthEnd} IS NULL OR m.birthDate <= :#{#filter.birthEnd}) " +
            "AND (:#{#filter.createAtStart} IS NULL OR m.createAt >= :#{#filter.createAtStart}) " +
            "AND (:#{#filter.createAtEnd} IS NULL OR m.createAt <= :#{#filter.createAtEnd}) " +
            "AND (:#{#filter.academicStatus} IS NULL OR m.academicStatus.codeId = :#{#filter.academicStatus}) " +
            "AND (:#{#filter.grade} IS NULL OR m.grade.codeId = :#{#filter.grade}) " +
            "AND (:#{#filter.startDateStart} IS NULL OR m.startDate >= :#{#filter.startDateStart}) " +
            "AND (:#{#filter.startDateEnd} IS NULL OR m.startDate <= :#{#filter.startDateEnd}) " +
            "AND (:#{#filter.endDateStart} IS NULL OR m.endDate >= :#{#filter.endDateStart}) " +
            "AND (:#{#filter.endDateEnd} IS NULL OR m.endDate <= :#{#filter.endDateEnd})")
    @EntityGraph(attributePaths = {"dept", "gender", "academicStatus", "grade", "memberType"})
    List<Member> findAllWithFilter(MemberSearchFilter filter);

    @Query("SELECT MAX(m.memberId) % 1000 FROM Member m where m.dept.deptId = :deptId " +
            "AND m.memberId BETWEEN (:year * 100000) and ((:year + 1) * 100000 - 1) " +
            "AND m.memberType.codeId = :memberType ")
    Integer getMaxStudentNoFromDeptAndYear(Long deptId, int year, Long memberType);

    @Query("SELECT MAX(m.memberId) % 100 FROM Member m where m.dept.deptId = :deptId " +
            "AND m.memberType.codeId = :memberType ")
    Integer getMaxProfessorNoFromDept(Long deptId, Long memberType);

    @Query("SELECT MAX(m.memberId) % 100 FROM Member m where m.dept.deptId = :deptId " +
            "AND m.memberType.codeId = :memberType ")
    Integer getMaxStaffNoFromDept(Long deptId, Long memberType);
}
