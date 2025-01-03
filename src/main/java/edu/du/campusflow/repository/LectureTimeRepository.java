package edu.du.campusflow.repository;

import edu.du.campusflow.entity.LectureTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureTimeRepository extends JpaRepository<LectureTime, Long> {
}
