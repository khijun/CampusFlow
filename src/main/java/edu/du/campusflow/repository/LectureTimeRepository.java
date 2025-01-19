package edu.du.campusflow.repository;

import edu.du.campusflow.entity.*;
import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.Facility;
import edu.du.campusflow.entity.Lecture;
import edu.du.campusflow.entity.LectureTime;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LectureTimeRepository extends JpaRepository<LectureTime, Long> {
    List<LectureTime> findByStartTimeAndEndTimeAndLectureWeek_Week(CommonCode startTime, CommonCode endTime, Integer week);
    LectureTime findByLectureWeek(LectureWeek lectureWeek);

    @Query("SELECT lt FROM LectureTime lt " +
            "JOIN FETCH lt.startTime " +
            "JOIN FETCH lt.endTime " +
            "WHERE lt.lectureWeek.week = :week " +
            "AND lt.lectureDay.codeValue = :lectureDay " +
            "AND lt.facility.facilityId = :facilityId")
    List<LectureTime> findOverlappingTimes(
            @Param("week") Integer week,
            @Param("lectureDay") String lectureDay,
            @Param("facilityId") Long facilityId
    );

    //강의 주차가 존재하는지 확인
    boolean existsByLectureWeek(LectureWeek lectureWeek);

    @EntityGraph(attributePaths = {"lectureWeek", "facility", "lectureDay", "startTime", "endTime"})
    List<LectureTime> findByLectureWeek_Lecture(Lecture lecture);

    @EntityGraph(attributePaths = {"lectureWeek.lecture.member.name", "lectureDay", "startTime", "endTime"})
    List<LectureTime> findByFacility(Facility facility);
}
