package com.kula.kula_project_backend.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * FollowingGroups is an entity class that represents a following group in the application.
 * It is annotated as a MongoDB document and is stored in the "following_groups" collection.
 */
@Data
@Document(collection = "following_groups")
@Accessors(chain = true)
public class FollowingGroups {

    /**
     * The id of the following group. It is the primary key in the "following_groups" collection.
     */
    @Id
    private ObjectId id;

    /**
     * The id of the user who owns the following group.
     */
    @Field("owner_id")
    private ObjectId ownerId;

    /**
     * An array of user ids that are included in the following group.
     */
    @Field("user_ids")
    private ObjectId[] userIds;
}
