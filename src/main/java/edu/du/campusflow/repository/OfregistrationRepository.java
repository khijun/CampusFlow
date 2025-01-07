package edu.du.campusflow.repository;

import edu.du.campusflow.entity.Member;
import edu.du.campusflow.entity.Ofregistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OfregistrationRepository extends JpaRepository<Ofregistration, Long> {
    @Query("SELECT o FROM Ofregistration o WHERE o.lectureId.member.memberId = :professorId")
    List<Ofregistration> findDistinctByLectureId_Member(@Param("professorId") Long professorId);
}