package com.kula.kula_project_backend.common;


import com.kula.kula_project_backend.entity.Posts;
import lombok.Data;

@Data
public class AggregationResult {
    private Posts post;
    private int commentsCount;

    // getters and setters
}