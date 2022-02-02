package com.keyword.keywordspring.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testing")
public class TestingApi {

    @GetMapping
    public String running() {
        return "Application running!";
    }
}
