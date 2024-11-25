package com.kula.kula_project_backend.dao;

import com.kula.kula_project_backend.entity.FollowingGroups;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
/**
 * FollowingGroupsRepository is a Spring Data MongoDB repository for the FollowingGroups entity.
 */
public interface FollowingGroupsRepository extends MongoRepository<FollowingGroups, ObjectId> {

    /**
     * Finds a following group by its owner's id.
     * @param ownerId The id of the owner.
     * @return An Optional that may contain the FollowingGroups object if found.
     */
    Optional<FollowingGroups> findByOwnerId(ObjectId ownerId);
}
