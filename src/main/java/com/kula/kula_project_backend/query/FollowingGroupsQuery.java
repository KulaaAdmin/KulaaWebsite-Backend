package com.kula.kula_project_backend.query;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * FollowingGroupsQuery is a data transfer object that represents a query for following groups.
 * It is used when retrieving following groups based on certain criteria.
 */
@Data
public class FollowingGroupsQuery {

    /**
     * The id of the following group.
     * Used to find a specific following group.
     */
    private ObjectId id;

    /**
     * The id of the user who owns the following group.
     * Used to find following groups owned by a specific user.
     */
    private ObjectId ownerId;

    /**
     * An array of user ids that are included in the following group.
     * Used to find following groups that include specific users.
     */
    private ObjectId[] userIds;
}
