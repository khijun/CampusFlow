package edu.du.campusflow.repository;

import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.Completion;
import edu.du.campusflow.entity.Lecture;
import edu.du.campusflow.entity.Ofregistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompletionRepository extends JpaRepository<Completion, Long> {
    List<Completion> findAllByOfRegistration(Ofregistration ofRegistration);
    boolean existsByOfRegistration(Ofregistration ofregistration);

    Optional<Completion> findByOfRegistration(Ofregistration ofregistration);
}
