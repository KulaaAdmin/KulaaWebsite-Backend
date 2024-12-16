package com.kula.kula_project_backend.dao;

import com.kula.kula_project_backend.entity.Posts;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
/**
 * PostsRepository is a Spring Data MongoDB repository for the Posts entity.
 */
@Repository
public interface PostsRepository extends MongoRepository<Posts, ObjectId> {
    /**
     * Finds a post by its id.
     * @param postId The id of the post.
     * @return An Optional that may contain the Posts object if found.
     */
    Optional<Posts> findById(ObjectId postId);
    // Find all posts by createdAt desc
    Page<Posts> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
