package com.kula.kula_project_backend.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * SharedPostLists is an entity class that represents a shared post list in the application.
 * It is annotated as a MongoDB document and is stored in the "shared_post_lists" collection.
 */
@Data
@Document(collection = "shared_post_lists")
@Accessors(chain = true)
public class SharedPostLists {

    /**
     * The id of the shared post list. It is the primary key in the "shared_post_lists" collection.
     */
    @Id
    private ObjectId id;

    /**
     * The id of the user who owns the shared post list.
     */
    @Field("user_id")
    private ObjectId userId;

    /**
     * The name of the collection that the shared post list belongs to.
     */
    @Field("collection_name")
    private String collectionName;

    /**
     * An array of post ids that are included in the shared post list.
     */
    @Field("post_ids")
    private ObjectId[] postIds;
}
