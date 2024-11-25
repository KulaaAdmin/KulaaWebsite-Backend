package com.kula.kula_project_backend.dao;

import com.kula.kula_project_backend.entity.Likes;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
/**
 * LikesRepository is a Spring Data MongoDB repository for the Likes entity.
 */
public interface LikesRepository extends MongoRepository<Likes, ObjectId> {
    /**
     * Finds a like by its target id.
     * @param targetId The id of the target.
     * @return An Optional that may contain the Likes object if found.
     */
    Optional<Likes> findByTargetId(ObjectId targetId);

    /**
     * Finds a like by its target id and target type.
     * @param postId The id of the post.
     * @param targetType The type of the target.
     * @return An Optional that may contain the Likes object if found.
     */
    Optional<Likes> findByTargetIdAndTargetType(ObjectId postId, String targetType);
}
