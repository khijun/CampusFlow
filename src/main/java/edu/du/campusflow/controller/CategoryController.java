package edu.du.campusflow.controller;

import edu.du.campusflow.entity.Category;
import edu.du.campusflow.service.AuthService;
import edu.du.campusflow.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {
    private final CategoryService categoryService;
    private final AuthService authService;

    public CategoryController(CategoryService categoryService, AuthService authService) {
        this.categoryService = categoryService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<?> getCategory() {
        List<Category> categories = categoryService.findByType(authService.getCurrentMember().getMemberType());
        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }
}
