package edu.du.campusflow.controller;

import edu.du.campusflow.dto.TuitionDTO;
import edu.du.campusflow.service.AuthService;
import edu.du.campusflow.service.TuitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class TuitionController {

    private final TuitionService tuitionService;
    private final AuthService authService;  // AuthService 추가

    @GetMapping("/iframe/tuition/admin/tuitionTarget")
    public String showTuitionTarget(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<TuitionDTO> tuitionPage = tuitionService.searchTuitionTargets(null, null, null, pageRequest);
        
        model.addAttribute("tuitionTargets", tuitionPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", tuitionPage.getTotalPages());
        
        return "view/iframe/tuition/admin/tuitionTarget";
    }

    @GetMapping("/api/tuition/search")
    @ResponseBody
    public Page<TuitionDTO> searchTuitionTargets(
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false) String studentName,
            @RequestParam(required = false) String department,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
            
        return tuitionService.searchTuitionTargets(
            studentId, 
            studentName, 
            department, 
            PageRequest.of(page, size)
        );
    }
} 