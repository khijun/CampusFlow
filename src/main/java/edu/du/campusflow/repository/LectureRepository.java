package edu.du.campusflow.repository;

import edu.du.campusflow.entity.Lecture;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long>, JpaSpecificationExecutor<Lecture> {
    @Query("SELECT l.lectureId FROM Lecture l WHERE l.member.memberId = :memberId")
    List<Long> findLectureIdsByMemberId(Long memberId);

    @Query("select l from Lecture l ")
    @EntityGraph(attributePaths = {"member", "curriculumSubject", "curriculumSubject.curriculum", "curriculumSubject.subjectType", "member.dept"})
    List<Lecture> findAllWithFetch();
}