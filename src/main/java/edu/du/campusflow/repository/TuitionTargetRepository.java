package edu.du.campusflow.repository;

import edu.du.campusflow.entity.TuitionTarget;
import edu.du.campusflow.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TuitionTargetRepository extends JpaRepository<TuitionTarget, Long> {
    Optional<TuitionTarget> findByMember(Member member);
}