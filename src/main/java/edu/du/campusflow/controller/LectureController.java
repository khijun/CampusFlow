package edu.du.campusflow.controller;

import edu.du.campusflow.entity.Lecture;
import edu.du.campusflow.service.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LectureController {

    @Autowired
    LectureService lectureService;

    @GetMapping("/api/lectures/search")
    public ResponseEntity<List<Lecture>> searchLectures(
            @RequestParam(required = false) String deptName,
            @RequestParam(required = false) String semester,
            @RequestParam(required = false) String subjectType,
            @RequestParam(required = false) String lectureName) {

        List<Lecture> lectures = lectureService.searchLectures(semester, subjectType, lectureName, deptName);
        return ResponseEntity.ok(lectures);
    }
}
