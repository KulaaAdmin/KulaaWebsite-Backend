package com.kula.kula_project_backend.dao;

import com.kula.kula_project_backend.entity.Regions;
import com.kula.kula_project_backend.entity.Tags;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface RegionsRepository extends MongoRepository<Regions, ObjectId> {
    /**
     * Finds a region by its id.
     * @param regionId The id of the region.
     * @return An Optional that may contain the Regions object if found.
     */
    @Override
    Optional<Regions> findById(ObjectId regionId);

    /**
     * Finds a region by its name.
     * @param regionName The name of the region.
     * @return An Optional that may contain the Regions object if found.
     */
    @Query("{ 'region_name' : ?0 }")
    Optional<Regions> findByRegionName(String regionName);
}
