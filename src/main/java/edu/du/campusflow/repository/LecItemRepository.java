package edu.du.campusflow.repository;

import edu.du.campusflow.entity.LecItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LecItemRepository extends JpaRepository<LecItem, Long> {
}