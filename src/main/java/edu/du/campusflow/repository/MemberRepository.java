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
    public List<Member> findByDept(Dept dept);

    @EntityGraph(attributePaths = {"memberType"})
    public Optional<Member> findById(Long id);

    @EntityGraph(attributePaths = {"memberType"})
    public List<Member> findAll();

    List<Member> findByAcademicStatus(CommonCode academicStatus); // CommonCode로 Member 찾기

    @Query("select m from Member m where m.memberType in :searchMemberType ")
    @EntityGraph(attributePaths = {"dept", "gender", "academicStatus", "grade", "memberType"})
    public List<Member> findAllWithDetails(List<CommonCode> searchMemberType);

}
