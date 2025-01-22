package edu.du.campusflow.controller;

import edu.du.campusflow.dto.AssignmentDTO;
import edu.du.campusflow.entity.Member;
import edu.du.campusflow.service.AssignmentService;
import edu.du.campusflow.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@Controller
public class AssignmentController {

    @Autowired
    AuthService authService;

    @Autowired
    AssignmentService assignmentService;

    @GetMapping("/iframe/assignment/create")
    public String create(Model model) {
        model.addAttribute("member", authService.getCurrentMember());
        return "/view/iframe/assignment/assignmentCreate";
    }

    //과제 생성 컨트롤러
    @PostMapping("/api/assignment/create")
    @ResponseBody
    public ResponseEntity<String> createAssignment(
            @ModelAttribute AssignmentDTO assignmentDTO,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            assignmentService.createAssignment(assignmentDTO, file);
            return ResponseEntity.ok("과제가 성공적으로 생성되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/iframe/assignment/student/submission")
    public String submission(Model model) {
        model.addAttribute("member", authService.getCurrentMember());
        return "/view/iframe/assignment/student/assignmentSubmission";
    }

    //강의아이디로 연결된 과제 리스트 검색
    @GetMapping("/api/assignment/student")
    @ResponseBody
    public ResponseEntity<List<AssignmentDTO>> getStudentAssignments(
            @RequestParam Long lectureId) {
        try {
            List<AssignmentDTO> assignments = assignmentService.getStudentAssignments(lectureId);
            return ResponseEntity.ok(assignments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
    }

    @PostMapping("/api/assignment/submit")
    @ResponseBody
    public ResponseEntity<String> submitAssignment(
            @RequestParam("file") MultipartFile file,
            @RequestParam("assignmentId") Long assignmentId) {
        try {
            // 파일 검증
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("제출할 파일을 선택해주세요.");
            }
            // 현재 로그인한 학생 정보 가져오기
            Member student = authService.getCurrentMember();
            // 과제 제출 서비스 호출
            assignmentService.submitAssignment(assignmentId, student, file);
            return ResponseEntity.ok("과제가 성공적으로 제출되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("과제 제출 중 오류가 발생했습니다.");
        }
    }
}
