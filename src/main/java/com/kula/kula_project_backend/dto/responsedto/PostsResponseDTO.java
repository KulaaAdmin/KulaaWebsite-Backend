package com.kula.kula_project_backend.dto.responsedto;

import com.kula.kula_project_backend.common.validator.SaveValidator;
import com.kula.kula_project_backend.common.validator.UpdateValidator;
import com.kula.kula_project_backend.entity.Posts;
import lombok.Data;
import org.bson.types.ObjectId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * PostsResponseDTO is a data transfer object for the response of a Posts entity.
 * It is used when returning a response after saving or updating posts.
 */
@Data
public class PostsResponseDTO implements Serializable {

    /**
     * The id of the post.
     */
    private String id;

    /**
     * The id of the author of the post.
     */
    private String authId;

    /**
     * The title of the post.
     */
    private String title;

    /**
     * The id of the dish associated with the post.
     */
    private String dishId;

    /**
     * The id of the Restaurant associated with the post.
     */
    private String restaurantId;

    /**
     * A list of tag ids associated with the post.
     */
    private ArrayList<String> tags;

    /**
     * The content of the post.
     */
    private String content;

    /**
     * The date and time when the post was created.
     */
    private Date createdAt;

    /**
     * The date and time when the post was last updated.
     */
    private Date updatedAt;

    /**
     * An array of image URLs associated with the post.
     */
    private String[] imageURL;

    /**
     * An array of image filenames associated with the post.
     */
    private ArrayList<String> images;

    /**
     * The negative review of the post.
     */
    private String negativeReview;

    /**
     * The positive review of the post.
     */
    private String positiveReview;

    /**
     * The video URL associated with the post.
     */
    private String videoURL;

    /**
     * The rating of the post.
     */
    private double rating;
}
