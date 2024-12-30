package edu.du.campusflow.service;

import edu.du.campusflow.entity.Post;
import edu.du.campusflow.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    // 모든 게시물 조회
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // 특정 게시물 조회
    public Post getPostById(Long postId) {
        return postRepository.findById(postId).orElse(null);
    }

    // 게시물 생성
    public Post createPost(Post post) {
        post.setCreatedAt(LocalDateTime.now()); // 생성 날짜 설정
        return postRepository.save(post);
    }

    // 게시물 업데이트
    public Post updatePost(Long postId, Post post) {
        Post existingPost = getPostById(postId);
        if (existingPost != null) {
            existingPost.setTitle(post.getTitle());
            existingPost.setContent(post.getContent());
            existingPost.setUpdatedAt(LocalDateTime.now()); // 업데이트 날짜 설정
            return postRepository.save(existingPost);
        }
        return null;
    }

    // 게시물 삭제
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }
}