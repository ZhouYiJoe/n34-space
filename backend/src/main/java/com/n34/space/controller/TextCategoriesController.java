package com.n34.space.controller;

import com.n34.space.utils.AiUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/textCategories")
public class TextCategoriesController {
    @GetMapping
    public List<String> getAllCategories() {
        return AiUtils.getAllCategories();
    }
}
