package com.n34.demo.controller;

import com.n34.demo.entity.Post;
import com.n34.demo.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("posts")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("{username}")
    public Map<String, Object> getAllPostsByUsername(@PathVariable String username) {
        return postService.getAllPostsByUsername(username);
    }

    @PostMapping("{username}")
    public Post addPost(@PathVariable String username, @RequestBody Post post) {
        return postService.addPost(username, post);
    }

    @DeleteMapping("{postId}")
    public boolean removePost(@PathVariable Long postId) {
        return postService.removePost(postId);
    }
}
