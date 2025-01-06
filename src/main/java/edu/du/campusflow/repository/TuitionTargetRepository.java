package edu.du.campusflow.repository;

import edu.du.campusflow.entity.TuitionTarget;
import edu.du.campusflow.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TuitionTargetRepository extends JpaRepository<TuitionTarget, Long> {
    Optional<TuitionTarget> findByMember(Member member);

    /**
     * 학번, 이름, 학과로 등록금 대상자를 검색하는 쿼리 메서드
     * @param studentId 학번 (선택적)
     * @param studentName 학생 이름 (선택적)
     * @param department 학과명 (선택적)
     * @param pageable 페이징 정보
     * @return 검색 조건에 맞는 등록금 대상자 페이지
     */
    @Query("SELECT t FROM TuitionTarget t JOIN t.member m JOIN m.dept d " +
           "WHERE (:studentId IS NULL OR m.memberId = :studentId) " +
           "AND (:studentName IS NULL OR m.name LIKE %:studentName%) " +
           "AND (:department IS NULL OR d.deptName LIKE %:department%)")
    Page<TuitionTarget> findBySearchCriteria(
            @Param("studentId") String studentId,
            @Param("studentName") String studentName,
            @Param("department") String department,
            Pageable pageable
    );
}