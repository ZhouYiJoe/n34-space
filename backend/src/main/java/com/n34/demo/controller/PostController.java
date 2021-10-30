package com.n34.demo.controller;

import com.n34.demo.response.Response;
import com.n34.demo.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("posts")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("{author}")
    public Response getAllPostsOfAuthor(@PathVariable String author) throws Exception {
        return postService.getPostsByUsername(author);
    }

    @PostMapping("{author}")
    public Response addPost(@PathVariable String author,
                            @RequestBody Map<String, Object> post) throws Exception {
        String body = (String) post.get("body");
        return postService.addPost(author, new Date(), body);
    }

    @DeleteMapping("{postId}")
    public Response removePost(@PathVariable Long postId) {
        return postService.removePost(postId);
    }

    @GetMapping
    public Response getAllPosts() throws Exception {
        return postService.getAllPosts();
    }
}
