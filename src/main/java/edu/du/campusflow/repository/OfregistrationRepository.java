package edu.du.campusflow.repository;

import edu.du.campusflow.entity.Member;
import edu.du.campusflow.entity.Ofregistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OfregistrationRepository extends JpaRepository<Ofregistration, Long> {
    @Query("SELECT o FROM Ofregistration o WHERE o.lectureId.member.memberId = :professorId")
    List<Ofregistration> findByLectureId_Member_MemberId(@Param("professorId") Long professorId);

    @Query("SELECT o FROM Ofregistration o WHERE o.lectureId.lectureId = :lectureId")
    List<Ofregistration> findByLectureId_LectureId(@Param("lectureId") Long lectureId);

    List<Ofregistration> findDistinctByLectureId_Member(Member member);

    @Query("SELECT DISTINCT o FROM Ofregistration o " +
            "JOIN FETCH o.lectureId l " +
            "JOIN FETCH l.curriculumSubject cs " +
            "JOIN FETCH cs.curriculum c " +     // curriculum 조인 추가
            "JOIN FETCH cs.subject s " +
            "JOIN FETCH s.dept d " +
            "JOIN FETCH o.member m " +
            "WHERE (:departmentId IS NULL OR d.deptId = :departmentId) " +
            "AND (:grade IS NULL OR c.grade.codeId = :grade) " +  // grade 조건 수정
            "AND (:lectureName IS NULL OR LOWER(l.lectureName) LIKE LOWER(CONCAT('%', :lectureName, '%'))) " +
            "AND (:studentName IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :studentName, '%')))")
    List<Ofregistration> findBySearchCriteria(
            @Param("departmentId") Long departmentId,
            @Param("grade") Integer grade,
            @Param("lectureName") String lectureName,
            @Param("studentName") String studentName
    );
}