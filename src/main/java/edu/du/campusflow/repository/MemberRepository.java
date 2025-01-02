package edu.du.campusflow.repository;

import edu.du.campusflow.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByMemberId(Long member);
}
