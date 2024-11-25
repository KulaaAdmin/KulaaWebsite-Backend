package com.kula.kula_project_backend.dao;

import com.kula.kula_project_backend.entity.SharedPostLists;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
/**
 * SharedPostListsRepository is a Spring Data MongoDB repository for the SharedPostLists entity.
 */
public interface SharedPostListsRepository extends MongoRepository<SharedPostLists, ObjectId> {
    // Future methods for the repository can be added here.
    // For example, to find shared post lists by a specific user, you might add:
    //
    // Optional<SharedPostLists> findByUserId(ObjectId userId);
    //
    // This method would return the shared post lists associated with a specific user.
}
