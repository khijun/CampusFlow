package edu.du.campusflow.repository;

import edu.du.campusflow.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Integer> {
    @Query("SELECT l.lectureId FROM Lecture l WHERE l.member.memberId = :memberId")
    List<Long> findLectureIdsByMemberId(Long memberId);
}
