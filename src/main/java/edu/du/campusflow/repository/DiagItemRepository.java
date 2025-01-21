package edu.du.campusflow.repository;

import edu.du.campusflow.entity.DiagItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiagItemRepository extends JpaRepository<DiagItem, Long> {
    // ofRegistration ID로 조회
    List<DiagItem> findByOfRegistration_Id(Long ofregistrationId);

    // 평가 존재 여부 확인
    boolean existsByOfRegistration_Id(Long ofregistrationId);
}