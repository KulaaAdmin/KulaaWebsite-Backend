package com.kula.kula_project_backend.dao;

import com.kula.kula_project_backend.entity.Areas;
import com.kula.kula_project_backend.entity.Regions;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface AreasRepository extends MongoRepository<Areas, ObjectId> {
    /**
     * Finds an area by its id.
     * @param areaId The id of the area.
     * @return An Optional that may contain the Areas object if found.
     */
    @Override
    Optional<Areas> findById(ObjectId areaId);

    /**
     * Finds an area by its name.
     * @param areaName The name of the area.
     * @return An Optional that may contain the Areas object if found.
     */
    @Query("{ 'area_name' : ?0 }")
    Optional<Areas> findByAreaName(String areaName);

    @Aggregation(pipeline = "{ $match: { 'region' : ?0 } }")
    List<Areas> findAreasbyRegionId(ObjectId regionId);
}
