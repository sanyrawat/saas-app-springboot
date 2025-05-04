package com.saasapp.saasApp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saasapp.saasApp.service.impl.RedisTestService;

@RestController
@RequestMapping("/api/redis")
public class RedisTestController {

    private final RedisTestService redisService;

    public RedisTestController(RedisTestService redisService) {
        this.redisService = redisService;
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        redisService.saveValue("hello", "world");
        String value = redisService.getValue("hello");
        return ResponseEntity.ok("Fetched from Redis: " + value);
    }
}
