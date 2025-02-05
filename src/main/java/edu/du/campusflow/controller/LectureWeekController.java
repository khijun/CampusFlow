package edu.du.campusflow.controller;

import edu.du.campusflow.dto.LectureDTO;
import edu.du.campusflow.dto.LectureWeekDTO;
import edu.du.campusflow.repository.CommonCodeGroupRepository;
import edu.du.campusflow.repository.CommonCodeRepository;
import edu.du.campusflow.service.AuthService;
import edu.du.campusflow.service.LectureWeekService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;

@Controller
public class LectureWeekController {

    @Autowired
    AuthService authService;

    @Autowired
    LectureWeekService lectureWeekService;

    @Autowired
    CommonCodeRepository commonCodeRepository;

    @Autowired
    CommonCodeGroupRepository commonCodeGroupRepository;

    //강의 주차 및 강의 시간 페이지
    @GetMapping("/iframe/lecture/lectureWeekTime")
    public String lectureWeekTime(Model model) {
        model.addAttribute("member", authService.getCurrentMember());
        model.addAttribute("classStatus",
                lectureWeekService.getClassStatusCodes());
        return "view/iframe/lecture/professor/lectureWeek_Time";
    }

    //강의 주차랑 시간 검색해서 불러오는 컨트롤러
    @GetMapping("/api/lecture/week-time")
    @ResponseBody
    public ResponseEntity<List<LectureWeekDTO>> getLecturesWeekTime(
            @RequestParam(required = false) String semesterCode,
            @RequestParam(required = false) String professorId,
            @RequestParam(required = false) String lectureName) {
        try {
            // 필수 파라미터 검증
            if (professorId == null || semesterCode == null || lectureName == null) {
                return ResponseEntity.badRequest().body(Collections.emptyList());
            }
            List<LectureWeekDTO> result = lectureWeekService.getLecturesWeekTime(semesterCode, professorId, lectureName);
            // 빈 리스트를 반환하도록 수정
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    //강의명 드롭다운으로 불러오는 컨트롤러
    @GetMapping("/api/lecture/professor-lectures")
    @ResponseBody
    public ResponseEntity<List<LectureDTO>> getProfessorLectures(
            @RequestParam String professorId,
            @RequestParam String semesterCode) {
        try {
            List<LectureDTO> lectures = lectureWeekService.getProfessorLectures(professorId, semesterCode);
            return ResponseEntity.ok(lectures);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    //수업 상태를 변경하는 컨트롤러
    @PostMapping("/api/lecture/update-status")
    @PreAuthorize("hasAnyRole('STAFF', 'PROFESSOR')")
    @ResponseBody
    public ResponseEntity<String> updateLectureStatus(
            @RequestParam Long lectureTimeId,
            @RequestParam String classStatus) {
        try {
            lectureWeekService.updateLectureStatus(lectureTimeId, classStatus);
            return ResponseEntity.ok("수업 상태가 성공적으로 변경되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}