package com.n34.demo.service;

import com.n34.demo.entity.Post;
import com.n34.demo.entity.User;
import com.n34.demo.repository.PostRepository;
import com.n34.demo.repository.UserRepository;
import com.n34.demo.response.Response;
import com.n34.demo.response.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostService(PostRepository postRepository,
                       UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Response addPost(String username, Post post) {
        Optional<User> user = userRepository.findById(username);
        if (user.isPresent()) {
            post.setAuthor(user.get());
            user.get().getPosts().add(post);
            postRepository.save(post);
            userRepository.save(user.get());
            return new Response(Status.SUCCESS, post);
        } else {
            return new Response(Status.USER_NOT_FOUND);
        }
    }

    @Transactional
    public Response removePost(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isPresent()) {
            postRepository.delete(post.get());
            return new Response(Status.SUCCESS);
        } else {
            return new Response(Status.POST_NOT_FOUND);
        }
    }

    public Response getAllPostsByUsername(String username) {
        Optional<User> user = userRepository.findById(username);
        if (user.isPresent()) {
            return new Response(Status.SUCCESS, user.get().getPosts().stream().toList());
        } else {
            return new Response(Status.USER_NOT_FOUND);
        }
    }
}
