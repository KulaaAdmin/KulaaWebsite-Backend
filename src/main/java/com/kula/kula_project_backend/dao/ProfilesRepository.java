package com.kula.kula_project_backend.dao;

import com.kula.kula_project_backend.entity.Profiles;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfilesRepository extends MongoRepository<Profiles, ObjectId> {
    /**
     * Find all profiles by location ID.
     *
     * @param locationId The location ID to filter profiles.
     * @return List of profiles matching the location ID.
     */
    List<Profiles> findByLocationId(ObjectId locationId);
}
