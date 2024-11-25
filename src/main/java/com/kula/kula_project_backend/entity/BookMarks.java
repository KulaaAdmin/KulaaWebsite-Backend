package com.kula.kula_project_backend.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
/**
 * BookMarks is an entity class that represents a bookmark in the application.
 * It is annotated as a MongoDB document and is stored in the "bookmarks" collection.
 */
@Data
@Document(collection = "bookmarks")
@Accessors(chain = true)
public class BookMarks implements Serializable {
    /**
     * The id of the bookmark. It is the primary key in the "bookmarks" collection.
     */
    @Id
    private ObjectId id;
    /**
     * The id of the user who owns the bookmark.
     */
    @Field("user_id")
    private ObjectId userId;
    /**
     * The name of the collection that the bookmark belongs to.
     */
    @Field("collection_name")
    private String collectionName;
    /**
     * An array of post ids that are included in the bookmark.
     */
    @Field("post_ids")
    private ObjectId[] postIds;

}
