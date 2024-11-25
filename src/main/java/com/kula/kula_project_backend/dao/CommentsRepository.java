package com.kula.kula_project_backend.dao;

import com.kula.kula_project_backend.entity.Comments;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
/**
 * CommentsRepository is a Spring Data MongoDB repository for the Comments entity.
 */
public interface CommentsRepository extends MongoRepository<Comments, ObjectId> {
    // Future methods for the repository can be added here.
    // For example, to find comments by a specific user or post, you might add:
    //
    // List<Comments> findByUserId(ObjectId userId);
    // List<Comments> findByPostId(ObjectId postId);
    //
    // These methods would return all comments made by a specific user or on a specific post, respectively.

}
