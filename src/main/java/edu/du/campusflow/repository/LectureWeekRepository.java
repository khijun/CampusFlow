package edu.du.campusflow.repository;

import edu.du.campusflow.entity.Lecture;
import edu.du.campusflow.entity.LectureWeek;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureWeekRepository extends JpaRepository<LectureWeek, Long> {
    List<LectureWeek> findByLecture(Lecture lecture);
}
