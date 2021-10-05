package com.n34.demo.service;

import com.n34.demo.entity.Post;
import com.n34.demo.entity.User;
import com.n34.demo.repository.PostRepository;
import com.n34.demo.repository.UserRepository;
import com.n34.demo.response.Response;
import com.n34.demo.response.Status;
import com.n34.demo.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    public Response addPost(String author, Date timeCreated, String body) throws Exception {
        User user = userRepository.findById(author).orElse(null);
        if (user != null) {
            Post post = new Post();
            post.setTimeCreated(timeCreated);
            post.setFilename(UUID.randomUUID().toString());
            post.setAuthor(user);
            user.getPosts().add(post);
            postRepository.save(post);
            userRepository.save(user);
            FileUtils.createPostFile(post.getFilename(), body);
            return new Response(Status.SUCCESS, Map.of("id", post.getId(),
                    "timeCreated", post.getTimeCreated(),
                    "body", body,
                    "author", post.getAuthor()));
        } else {
            return new Response(Status.USER_NOT_FOUND);
        }
    }

    @Transactional
    public Response removePost(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post != null) {
            FileUtils.deletePostFile(post.getFilename());
            postRepository.delete(post);
            return new Response(Status.SUCCESS);
        } else {
            return new Response(Status.POST_NOT_FOUND);
        }
    }

    public Response getPostsByUsername(String username) throws Exception {
        User user = userRepository.findById(username).orElse(null);
        if (user != null) {
            List<Post> posts = user.getPosts().stream().toList();
            List<Map<String, Object>> postsInfo = new ArrayList<>();
            for (Post post : posts) {
                postsInfo.add(Map.of("id", post.getId(),
                        "timeCreated", post.getTimeCreated(),
                        "body", FileUtils.getPostFileContent(post.getFilename()),
                        "author", post.getAuthor()));
            }
            return new Response(Status.SUCCESS, postsInfo);
        } else {
            return new Response(Status.USER_NOT_FOUND);
        }
    }
}
