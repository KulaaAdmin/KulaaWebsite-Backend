package com.kula.kula_project_backend.service;

import com.kula.kula_project_backend.dao.PostsRepository;
import com.kula.kula_project_backend.entity.Posts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;


@Service
public class FeedService {
    private final PostsRepository postsRepository;

    @Autowired
    public FeedService(PostsRepository postsRepository) {
        this.postsRepository = postsRepository;
    }

    public Page<Posts> getFeed(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);

        return postsRepository.findAllByOrderByCreatedAtDesc(pageable);
    }
}
