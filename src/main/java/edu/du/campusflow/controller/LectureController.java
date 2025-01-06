package edu.du.campusflow.controller;

import edu.du.campusflow.dto.LectureDTO;
import edu.du.campusflow.entity.Lecture;
import edu.du.campusflow.service.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LectureController {

    @Autowired
    LectureService lectureService;

    @PostMapping("/api/lecture/create")
    public ResponseEntity<String> createLecture(@RequestBody LectureDTO lectureDTO) {
        try {
            lectureService.createLecture(lectureDTO);
            return ResponseEntity.ok("강의가 성공적으로 생성되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
