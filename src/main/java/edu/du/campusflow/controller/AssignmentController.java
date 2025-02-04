package edu.du.campusflow.controller;

import edu.du.campusflow.dto.AssignmentDTO;
import edu.du.campusflow.entity.Member;
import edu.du.campusflow.service.AssignmentService;
import edu.du.campusflow.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AssignmentController {

    @Autowired
    AuthService authService;

    @Autowired
    AssignmentService assignmentService;

    @GetMapping("/iframe/assignment/create")
    public String create(Model model) {
        model.addAttribute("member", authService.getCurrentMember());
        return "view/iframe/assignment/assignmentCreate";
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
        return "view/iframe/assignment/student/assignmentSubmission";
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

    @GetMapping("/iframe/assignment/submissionList")
    public String submissionList(Model model) {
        model.addAttribute("member", authService.getCurrentMember());
        return "view/iframe/assignment/submissionList";
    }

    @GetMapping("/api/assignment/professor/list")
    @ResponseBody
    public ResponseEntity<List<AssignmentDTO>> getProfessorAssignments(
            @RequestParam String semesterCode,
            @RequestParam String professorId,
            @RequestParam(required = false) String lectureId) {
        try {
            List<AssignmentDTO> assignments = assignmentService.getProfessorAssignments(
                    semesterCode, professorId, lectureId);
            return ResponseEntity.ok(assignments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
    }

    @GetMapping("/api/assignment/submissions/{assignmentId}")
    @ResponseBody
    public ResponseEntity<List<AssignmentDTO>> getSubmissionList(@PathVariable Long assignmentId) {
        try {
            List<AssignmentDTO> submissions = assignmentService.getSubmissionList(assignmentId);
            return ResponseEntity.ok(submissions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
    }

    @GetMapping("/api/file/submissionViewer/{fileId}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> getFileViewer(@PathVariable Long fileId) {
        try {
            String viewerHtml = assignmentService.getFileViewerHtml(fileId);
            Map<String, String> response = new HashMap<>();
            response.put("html", viewerHtml);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @GetMapping("/api/file/submission/{fileId}")
    @ResponseBody
    public ResponseEntity<Resource> viewSubmissionFile(@PathVariable Long fileId) {
        try {
            return assignmentService.getSubmissionFileResponse(fileId);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }

    //과제 점수 등록 컨트롤러
    @PostMapping("/api/assignment/score")
    @ResponseBody
    public ResponseEntity<String> updateAssignmentScore(@RequestBody Map<String, Object> request) {
        try {
            Long submissionId = Long.parseLong(request.get("submissionId").toString());
            Integer score = Integer.parseInt(request.get("score").toString());

            assignmentService.updateSubmissionScore(submissionId, score);
            return ResponseEntity.ok("점수가 성공적으로 등록되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
