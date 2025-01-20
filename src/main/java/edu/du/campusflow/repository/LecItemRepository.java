package edu.du.campusflow.repository;

import edu.du.campusflow.entity.LecItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LecItemRepository extends JpaRepository<LecItem, Long> {
    // ofRegistration ID로 조회
    List<LecItem> findByOfRegistration_Id(Long ofregistrationId);

    // 평가 존재 여부 확인
    boolean existsByOfRegistration_Id(Long ofregistrationId);
}