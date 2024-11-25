package com.kula.kula_project_backend.dao;

import com.kula.kula_project_backend.entity.Profiles;
import org.bson.types.ObjectId;

import org.springframework.data.mongodb.repository.MongoRepository;
/**
 * ProfilesRepository is a Spring Data MongoDB repository for the Profiles entity.
 */
public interface ProfilesRepository extends MongoRepository<Profiles, ObjectId> {
    // Future methods for the repository can be added here.
    // For example, to find profiles by a specific user, you might add:
    //
    // Optional<Profiles> findByUserId(ObjectId userId);
    //
    // This method would return the profile associated with a specific user.
}
