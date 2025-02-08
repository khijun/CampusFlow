package edu.du.campusflow.repository;

import edu.du.campusflow.entity.Lecture;
import edu.du.campusflow.entity.LectureWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LectureWeekRepository extends JpaRepository<LectureWeek, Long> {
    List<LectureWeek> findByLecture(Lecture lecture);

    @Query("SELECT lw FROM LectureWeek lw WHERE lw.lecture.lectureId = :lectureId")
    List<LectureWeek> findByLectureId(@Param("lectureId") Long lectureId);
}
