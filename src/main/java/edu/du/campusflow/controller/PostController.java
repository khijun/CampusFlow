package edu.du.campusflow.controller;

import edu.du.campusflow.entity.Post;
import edu.du.campusflow.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    // 게시물 추가 페이지
    @GetMapping("/add")
    public String showAddPostForm() {
        return "posts/addPost"; // 게시물 추가 페이지로 이동
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
        model.addAttribute("posts", postService.getAllPosts()); // 모든 게시물을 모델에 추가
        return "posts/viewPosts"; // 게시물 목록 페이지로 이동
    }

    // 특정 게시물 상세 조회 페이지
    @GetMapping("/{id}")
    public String viewPostDetail(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id);
        model.addAttribute("post", post); // 특정 게시물을 모델에 추가
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
}