package com.n34.demo.service;

import com.n34.demo.entity.Post;
import com.n34.demo.entity.User;
import com.n34.demo.repository.PostRepository;
import com.n34.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

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
    public Post addPost(String username, Post post) {
        User user = userRepository.findById(username).orElse(null);
        if (user == null) {
            return null;
        }
        post.setAuthor(user);
        user.getPosts().add(post);
        postRepository.save(post);
        userRepository.save(user);
        return post;
    }

    @Transactional
    public boolean removePost(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) {
            return false;
        }
        postRepository.delete(post);
        return true;
    }

    public Map<String, Object> getAllPostsByUsername(String username) {
        User user = userRepository.findById(username).orElse(null);
        if (user == null) {
            return Map.of("userFound", false);
        }
        return Map.of("userFound", true,
                "posts", user.getPosts().stream().toList());
    }
}
