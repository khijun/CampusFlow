package edu.du.campusflow.repository;

import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.Dept;
import edu.du.campusflow.entity.Member;
import edu.du.campusflow.enums.AcademicStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    public List<Member> findByDept(Dept dept);

    List<Member> findByAcademicStatus(CommonCode academicStatus); // CommonCode로 Member 찾기
}
