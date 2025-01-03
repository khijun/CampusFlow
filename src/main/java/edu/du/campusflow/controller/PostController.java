package edu.du.campusflow.controller;

import edu.du.campusflow.entity.Dept;
import edu.du.campusflow.entity.Post;
import edu.du.campusflow.service.DeptService;
import edu.du.campusflow.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private DeptService departmentService; // 추가

    // 게시물 추가 페이지
    @GetMapping("/add")
    public String showAddPostForm(Model model) {
        model.addAttribute("post", new Post()); // Post 객체 추가
        return "posts/addPost"; // 템플릿 이름
    }

    // 게시물 추가 처리
    @PostMapping
    public String addPost(Post post) {
        postService.createPost(post); // 게시물 생성
        return "redirect:/posts/view"; // 게시물 추가 후 목록 페이지로 리다이렉트
    }

    // 모든 게시물 조회 페이지
    @GetMapping("/view")
    public String viewPosts(Model model) {
        List<Dept> departments = departmentService.getAllDepartments(); // 모든 학과 가져오기
        model.addAttribute("departments", departments); // 학과 목록 추가

        Map<Long, List<Post>> postsByDepartment = new HashMap<>(); // 학과별 게시물 목록 저장

        // 각 학과에 대한 게시물 목록을 추가
        for (Dept dept : departments) {
            List<Post> posts = postService.getPostsByDepartment(dept.getDeptId());
            postsByDepartment.put(dept.getDeptId(), posts); // 학과 ID에 따라 게시물 목록 추가
        }

        model.addAttribute("postsByDepartment", postsByDepartment); // 모델에 추가

        return "posts/viewPosts"; // 게시물 목록 페이지로 이동
    }

    // 특정 게시물 상세 조회 페이지
    @GetMapping("/{id}")
    public String viewPost(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id); // 게시물 ID로 게시물 조회
        if (post == null) {
            // 게시물이 존재하지 않을 경우 처리
            return "redirect:/posts/view"; // 게시물 목록 페이지로 리다이렉트
        }
        model.addAttribute("post", post); // 모델에 게시물 추가
        return "posts/postDetail"; // 게시물 상세 페이지로 이동
    }
    // 게시물 수정 페이지
    @GetMapping("/edit/{id}")
    public String showEditPostForm(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id);
        model.addAttribute("post", post); // 수정할 게시물을 모델에 추가
        return "posts/editPost"; // 게시물 수정 페이지로 이동
    }

    // 게시물 수정 처리
    @PostMapping("/edit/{id}")
    public String editPost(@PathVariable Long id, Post post) {
        postService.updatePost(id, post); // 게시물 업데이트
        return "redirect:/posts/view"; // 게시물 수정 후 목록 페이지로 리다이렉트
    }

    // 게시물 삭제 처리
    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable Long id) {
        postService.deletePost(id); // 게시물 삭제
        return "redirect:/posts/view"; // 게시물 삭제 후 목록 페이지로 리다이렉트
    }
    // 댓글 추가
    @PostMapping("/{id}/comments")
    public String addComment(@PathVariable Long id, @RequestParam String content) {
        postService.addComment(id, content); // 댓글 추가 서비스 호출
        return "redirect:/posts/" + id; // 댓글 추가 후 게시물 상세 페이지로 리다이렉트
    }
}