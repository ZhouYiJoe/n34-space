package com.n34.space.controller;

import io.jsonwebtoken.lang.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/exceptionHandler")
@RestController
public class ExceptionController {
    public void handleException(HttpServletRequest request) {
        Exception e = (Exception) request.getAttribute("exception");
        throw new RuntimeException(e.getMessage());
    }
}
