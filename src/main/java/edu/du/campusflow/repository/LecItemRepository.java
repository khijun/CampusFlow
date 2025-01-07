package edu.du.campusflow.repository;

import edu.du.campusflow.entity.LecItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LecItemRepository extends JpaRepository<LecItem, Long> {

    List<LecItem> findByOfRegistration_Id(Long ofregistrationId);

    List<LecItem> findByLecQuestion_QuestionId(Long questionId);

    List<LecItem> findByOfRegistration_IdInAndLecQuestion_QuestionId(List<Long> ofregistrationIds, Long questionId);

    boolean existsByOfRegistration_Id(Long ofregistrationId);
}