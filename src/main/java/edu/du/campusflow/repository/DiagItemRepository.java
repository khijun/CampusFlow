package edu.du.campusflow.repository;

import edu.du.campusflow.entity.DiagItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiagItemRepository extends JpaRepository<DiagItem, Long> {
    List<DiagItem> findByOfRegistration_Id(Long ofregistrationId);
    List<DiagItem> findByDiagQuestion_QuestionId(Long questionId);
    boolean existsByOfRegistration_Id(Long ofregistrationId);

    List<DiagItem> findByOfRegistration_IdAndDiagQuestion_QuestionId(
            Long ofregistrationId,
            Long questionId
    );
}