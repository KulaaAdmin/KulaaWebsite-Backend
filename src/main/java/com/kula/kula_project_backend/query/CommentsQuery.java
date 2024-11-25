package com.kula.kula_project_backend.query;

import lombok.Data;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * CommentsQuery is a data transfer object that represents a query for comments.
 * It is used when retrieving comments based on certain criteria.
 */
@Data
public class CommentsQuery {
    /**
     * The id of the author of the comment.
     * Used to find comments made by a specific user.
     */
    private ObjectId authId;

    /**
     * The id of the post that the comment is associated with.
     * Used to find comments associated with a specific post.
     */
    private ObjectId postId;

    /**
     * The content of the comment.
     * Used to find comments with specific content.
     */
    private String content;

    /**
     * The date and time when the comment was created.
     * Used to find comments created at a specific time.
     */
    private Date createdAt;

    /**
     * The date and time when the comment was last updated.
     * Used to find comments updated at a specific time.
     */
    private Date updatedAt;
}
