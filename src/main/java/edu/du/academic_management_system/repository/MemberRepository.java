package edu.du.academic_management_system.repository;

import edu.du.academic_management_system.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
