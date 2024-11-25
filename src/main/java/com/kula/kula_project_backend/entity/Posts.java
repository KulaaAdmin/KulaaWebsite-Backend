package com.kula.kula_project_backend.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Posts is an entity class that represents a post in the application.
 * It is annotated as a MongoDB document and is stored in the "posts" collection.
 */
@Data
@Document(collection = "posts")
@Accessors(chain = true)
public class Posts implements Serializable {

    /**
     * The id of the post. It is the primary key in the "posts" collection.
     */
    @Id
    private ObjectId id;

    /**
     * The id of the author of the post.
     */
    @Field("auth_id")
    private ObjectId authId;

    /**
     * The title of the post.
     */
    @Field("title")
    private String title;

    /**
     * The id of the dish associated with the post.
     */
    @Field("dish_id")
    private ObjectId dishId;


	/**
	 * The id of the dish associated with the post.
	 */
	@Field("restaurant_id")
	private ObjectId restaurantId;
    /**
	 * 
	 * An array of tag ids associated with the post.
	 */
    @Field("tags")
    private ArrayList<ObjectId> tags;

    /**
     * The content of the post.
     */
    @Field("content")
    private String content;

    /**
     * The date and time when the post was created.
     */
    @Field("created_at")
    private Date createdAt;

    /**
     * The date and time when the post was last updated.
     */
    @Field("updated_at")
    private Date updatedAt;

    /**
     * An array of image URLs associated with the post.
     */
    @Field("image_URL")
    private String[] imageURL;

    /**
     * An array of image filenames associated with the post.
     */
    @Field("images")
    private ArrayList<String> images;

    /**
     * The negative review of the post.
     */
    @Field("negative_review")
    private String negativeReview;

    /**
     * The positive review of the post.
     */
    @Field("positive_review")
    private String positiveReview;

    /**
     * The video URL associated with the post.
     */
    @Field("video_URL")
    private String videoURL;

    /**
     * The rating of the post.
     */
    @Field("rating")
    private int rating;
}
