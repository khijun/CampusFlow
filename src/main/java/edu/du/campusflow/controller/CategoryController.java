package edu.du.campusflow.controller;

import edu.du.campusflow.dto.CategoryDTO;
import edu.du.campusflow.service.AuthService;
import edu.du.campusflow.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<?> getCategory() {
        List<CategoryDTO> categories = categoryService.findByType(authService.getCurrentMember());
        return ResponseEntity.ok(categories);
    }
}
