package edu.du.campusflow.controller;

import edu.du.campusflow.dto.AssignmentDTO;
import edu.du.campusflow.service.AssignmentService;
import edu.du.campusflow.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

}
