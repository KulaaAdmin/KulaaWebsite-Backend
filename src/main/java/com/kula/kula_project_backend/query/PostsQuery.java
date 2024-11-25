package com.kula.kula_project_backend.query;

import lombok.Data;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * PostsQuery is a data transfer object that represents a query for posts.
 * It is used when retrieving posts based on certain criteria.
 */
@Data
public class PostsQuery {
    /**
     * The id of the author of the post.
     * Used to find posts made by a specific user.
     */
    private ObjectId authId;

    /**
     * The title of the post.
     * Used to find posts with a specific title.
     */
    private String title;

    /**
     * The id of the dish associated with the post.
     * Used to find posts associated with a specific dish.
     */
    private String dishId;

    /**
     * An array of tag ids associated with the post.
     * Used to find posts associated with specific tags.
     */
    private String[] tags;

    /**
     * The content of the post.
     * Used to find posts with specific content.
     */
    private String content;

    /**
     * The date and time when the post was created.
     * Used to find posts created at a specific time.
     */
    private Date createdAt;

    /**
     * The date and time when the post was last updated.
     * Used to find posts updated at a specific time.
     */
    private Date updatedAt;

    /**
     * The rating of the post.
     * Used to find posts with a specific rating.
     */
    private int rating;

    /**
     * The start date of the time range for the post creation date.
     * Used in combination with endDate to find posts created within a specific time range.
     */
    private Date startDate;

    /**
     * The end date of the time range for the post creation date.
     * Used in combination with startDate to find posts created within a specific time range.
     */
    private Date endDate;
}
