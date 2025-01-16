package edu.du.campusflow.repository;

import edu.du.campusflow.entity.ChangeHistory;
import edu.du.campusflow.entity.ChangeRequest;
import edu.du.campusflow.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChangeHistoryRepository extends JpaRepository<ChangeHistory,Long> {
    // Member의 memberId로 조회
    @EntityGraph(attributePaths = {
//            "member.dept.curriculums.curriculumStatus", "member.dept.deptStatus", "member.dept.curriculums.grade", "member.dept.curriculums.dayNight", "member.dept.curriculums.gradingMethod",
            "beforeCode", "afterCode", "grade"})
    List<ChangeHistory> findAll();

    List<ChangeHistory> findByMember_MemberId(Long memberId);

    @Query("SELECT m FROM Member m WHERE m.memberType.codeId = :memberType")
    List<Member> findMembersByType(@Param("memberType") Long memberType);
}
