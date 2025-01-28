package edu.du.campusflow.repository;

import edu.du.campusflow.dto.LectureDTO;
import edu.du.campusflow.dto.LectureListDTO;
import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.Lecture;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long>, JpaSpecificationExecutor<Lecture> {
    @Query("SELECT l.lectureId FROM Lecture l WHERE l.member.memberId = :memberId")
    List<Long> findLectureIdsByMemberId(Long memberId);

    @Query("select l from Lecture l ")
    @EntityGraph(attributePaths = {"member", "curriculumSubject", "curriculumSubject.curriculum", "curriculumSubject.subjectType", "member.dept"})
    List<Lecture> findAllWithFetch();

    @EntityGraph(attributePaths = {
            "curriculumSubject.curriculum.dept"
    })
    @Query("SELECT l FROM Lecture l " +
            "WHERE l.curriculumSubject.curriculum.dept.deptId = :deptId")
    List<Lecture> findByDepartmentId(@Param("deptId") Long deptId);

    @Query("SELECT l FROM Lecture l " +
            "WHERE l.member.memberId = :professorId " +
            "AND l.semester = :semester " +
            "AND l.lectureStatus.codeValue = 'LECTURE_PENDING'" +
            "OR l.lectureStatus.codeValue = 'LECTURE_STARTED'" )
    @EntityGraph(attributePaths = {"member"})
    List<Lecture> findByMember_MemberIdAndSemester(
            @Param("professorId") String professorId,
            @Param("semester") CommonCode semester
    );

    List<Lecture> findByMember_MemberId(@Param("memberId") Long professorId);

}