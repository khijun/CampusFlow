package edu.du.campusflow.repository;

import edu.du.campusflow.entity.Member;
import edu.du.campusflow.entity.TuitionTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.lang.annotation.Target;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface TuitionTargetRepository extends JpaRepository<TuitionTarget, Long> {
    TuitionTarget findByMember(Member member);

    @EntityGraph(attributePaths = {"tuitionId", "member", "member.dept"})
    List<TuitionTarget> findAll();
    /**
     * 검색 조건에 따른 등록금 대상자 조회
     */
    @Query("SELECT t FROM TuitionTarget t " +
           "WHERE (:studentId IS NULL OR t.member.memberId = :studentId) " +
           "AND (:studentName IS NULL OR t.member.name LIKE %:studentName%) " +
           "AND (:department IS NULL OR t.member.dept.deptName LIKE %:department%)")
    Page<TuitionTarget> findBySearchCriteria(
            @Param("studentId") String studentId,
            @Param("studentName") String studentName,
            @Param("department") String department,
            Pageable pageable);
}
