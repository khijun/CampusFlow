package edu.du.campusflow.repository;

import edu.du.campusflow.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
