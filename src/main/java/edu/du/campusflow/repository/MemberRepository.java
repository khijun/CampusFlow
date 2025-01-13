package edu.du.campusflow.repository;

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

    @EntityGraph(attributePaths = {"memberType"})
    Optional<Member> findById(Long id);

    @EntityGraph(attributePaths = {"memberType"})
    List<Member> findAll();

    List<Member> findByAcademicStatus(CommonCode academicStatus); // CommonCode로 Member 찾기

    @Query("select m from Member m where m.memberType.codeId in :typeIds ")
    @EntityGraph(attributePaths = {"dept", "gender", "academicStatus", "grade", "memberType"})
    List<Member> findAllWithDetailsByIds(List<Long> typeIds);

    @Query("select m from Member m")
    @EntityGraph(attributePaths = {"dept", "gender", "academicStatus", "grade", "memberType"})
    List<Member> findAllWithDetails();

}
