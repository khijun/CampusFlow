package edu.du.campusflow.repository;

import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.Facility;
import edu.du.campusflow.entity.Lecture;
import edu.du.campusflow.entity.LectureTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureTimeRepository extends JpaRepository<LectureTime, Long> {
    List<LectureTime> findByStartTimeAndEndTimeAndLectureWeek_Week(CommonCode startTime, CommonCode endTime, Integer week);
    List<LectureTime> findByLectureWeek_Lecture(Lecture lecture);
}
