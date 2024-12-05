package com.kula.kula_project_backend.controller;

import com.kula.kula_project_backend.entity.Posts;
import com.kula.kula_project_backend.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/feed")
public class FeedController {
    private final FeedService feedService;

    @Autowired
    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @GetMapping("/{page}")
    public ResponseEntity<Page<Posts>> getFeed(
            @PathVariable int page,
            @RequestParam(defaultValue = "5") int pageSize) {
        if (page < 0 || pageSize <= 0) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(feedService.getFeed(page, pageSize));
    }
}
