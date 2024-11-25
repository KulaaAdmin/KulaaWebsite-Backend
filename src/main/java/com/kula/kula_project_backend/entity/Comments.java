package com.kula.kula_project_backend.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;
/**
 * Comments is an entity class that represents a comment in the application.
 * It is annotated as a MongoDB document and is stored in the "comments" collection.
 */
@Data
@Document(collection = "comments")
@Accessors(chain = true)
public class Comments implements Serializable {

    /**
     * The id of the comment. It is the primary key in the "comments" collection.
     */
    @Id
    private ObjectId id;

    /**
     * The id of the post that the comment is associated with.
     */
    @Field("post_id")
    private ObjectId postId;

    /**
     * The content of the comment.
     */
    @Field("content")
    private String content;

    /**
     * The date and time when the comment was created.
     */
    @Field("created_at")
    private Date createdAt;

    /**
     * The date and time when the comment was last updated.
     */
    @Field("updated_at")
    private Date updatedAt;

    /**
     * The id of the user who made the comment.
     */
    @Field("user_id")
    private ObjectId userId;
}
