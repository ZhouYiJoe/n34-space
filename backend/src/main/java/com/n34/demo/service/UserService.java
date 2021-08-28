package com.n34.demo.service;

import com.n34.demo.entity.User;
import com.n34.demo.repository.UserRepository;
import com.n34.demo.util.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class UserService {
    enum State {
        USER_NOT_EXISTS,
        WRONG_PASSWORD,
        LOGIN_SUCCESSFULLY,
        USERNAME_ALREADY_EXISTS,
        EMAIL_ALREADY_EXISTS,
        REGISTER_SUCCESSFULLY
    }

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Map<String, Object> checkLogin(String username, String password) {
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            return Map.of("state", State.USER_NOT_EXISTS);
        } else if (!passwordEncoder.matches(password, user.getPassword())) {
            return Map.of("state", State.WRONG_PASSWORD);
        }

        return Map.of("state", State.LOGIN_SUCCESSFULLY,
                "token", JwtUtils.createToken(username));
    }

    public Map<String, Object> addUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return Map.of("state", State.USERNAME_ALREADY_EXISTS);
        } else if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return Map.of("state", State.EMAIL_ALREADY_EXISTS);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return Map.of("state", State.REGISTER_SUCCESSFULLY);
    }
}
