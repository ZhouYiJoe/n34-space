package com.n34.demo.service;

import com.n34.demo.entity.User;
import com.n34.demo.repository.UserRepository;
import com.n34.demo.response.Response;
import com.n34.demo.response.Status;
import com.n34.demo.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Response checkLogin(String username, String password) {
        Optional<User> user = userRepository.findById(username);

        if (user.isEmpty()) {
            return new Response(Status.USER_NOT_FOUND);
        } else if (!passwordEncoder.matches(password, user.get().getPassword())) {
            return new Response(Status.WRONG_PASSWORD);
        }

        return new Response(Status.SUCCESS, JwtUtils.createToken(username));
    }

    @Transactional
    public Response addUser(User user) {
        if (userRepository.findById(user.getUsername()).isPresent()) {
            return new Response(Status.REPEATED_USERNAME);
        } else if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return new Response(Status.REPEATED_EMAIL);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return new Response(Status.SUCCESS);
    }

    public Response getAllUsers() {
        return new Response(Status.SUCCESS,userRepository.findAll());
    }

    public Response getUserByUsername(String username) {
        Optional<User> user = userRepository.findById(username);
        if (user.isPresent()) {
            return new Response(Status.SUCCESS, user.get());
        } else {
            return new Response(Status.USER_NOT_FOUND);
        }
    }
}
