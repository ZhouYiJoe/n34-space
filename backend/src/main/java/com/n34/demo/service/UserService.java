package com.n34.demo.service;

import com.n34.demo.entity.User;
import com.n34.demo.repository.UserRepository;
import com.n34.demo.response.Response;
import com.n34.demo.response.Status;
import com.n34.demo.utils.JwtUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Response checkLogin(String username, String password) {
        User user = userRepository.findById(username).orElse(null);
        if (user == null) {
            return new Response(Status.USER_NOT_FOUND);
        } else if (!BCrypt.checkpw(password, user.getPassword())) {
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
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        userRepository.save(user);
        return new Response(Status.SUCCESS);
    }

    public Response getAllUsers() {
        return new Response(Status.SUCCESS,userRepository.findAll());
    }

    public Response getUserByUsername(String username) {
        User user = userRepository.findById(username).orElse(null);
        if (user != null) {
            return new Response(Status.SUCCESS, user);
        } else {
            return new Response(Status.USER_NOT_FOUND);
        }
    }
}
