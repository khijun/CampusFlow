package edu.du.campusflow.repository;

import edu.du.campusflow.entity.Assignment;
import edu.du.campusflow.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    //강의의 모든 과제 조회
    List<Assignment> findByLecture(Lecture lecture);
}
