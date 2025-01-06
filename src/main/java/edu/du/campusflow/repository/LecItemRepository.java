package edu.du.campusflow.repository;

import edu.du.campusflow.entity.LecItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LecItemRepository extends JpaRepository<LecItem, Long> {
    // ofregistration_id -> id로 변경
    List<LecItem> findByOfRegistration_Id(Long ofregistrationId);
    List<LecItem> findByLecQuestion_QuestionId(Long questionId);
    boolean existsByOfRegistration_Id(Long ofregistrationId);

    // 여기도 수정
    List<LecItem> findByOfRegistration_IdAndLecQuestion_QuestionId(Long ofregistrationId, Long questionId);
}