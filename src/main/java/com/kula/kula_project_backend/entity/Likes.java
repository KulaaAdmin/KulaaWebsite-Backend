package com.kula.kula_project_backend.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * Likes is an entity class that represents a like in the application.
 * It is annotated as a MongoDB document and is stored in the "likes" collection.
 */
@Data
@Document(collection = "likes")
@Accessors(chain = true)
public class Likes implements Serializable {

    /**
     * The id of the target (e.g., a post or a comment) that the like is associated with.
     * It is the primary key in the "likes" collection.
     */
    @Id
    private ObjectId targetId;

    /**
     * The type of the target that the like is associated with (e.g., "post" or "comment").
     */
    @Field
    private String targetType;

    /**
     * An array of user ids that have liked the target.
     */
    @Field("like_user")
    private ObjectId[] userIds;
}
