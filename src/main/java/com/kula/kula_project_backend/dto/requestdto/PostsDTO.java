package com.kula.kula_project_backend.dto.requestdto;

import com.kula.kula_project_backend.common.validator.SaveValidator;
import com.kula.kula_project_backend.common.validator.UpdateValidator;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.web.multipart.MultipartFile;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;

/**
 * PostsDTO is a data transfer object for the Posts entity.
 * It is used when saving or updating posts.
 */
@Data
public class PostsDTO {
    /**
     * The id of the post.
     * It is required when updating a post.
     */
    @NotNull(message = "id cannot be null", groups = {UpdateValidator.class})
    private ObjectId id;

    /**
     * The id of the author of the post.
     * It is required when saving a post.
     */
    @NotNull(message = "authId cannot be blank", groups = {SaveValidator.class})
    private ObjectId authId;

    /**
     * The title of the post.
     * It is required when saving a post.
     */
    //@NotBlank(message = "title cannot be blank", groups = {SaveValidator.class})
    private String title;

    /**
     * The id of the dish associated with the post.
     */
    private ObjectId dishId;

    /**
     * The id of the Restaurant associated with the post.
     */
    private ObjectId restaurantId;

    /**
     * An array of tag ids associated with the post.
     */
    private ArrayList<String> tags;

    /**
     * The content of the post.
     * It is required when saving a post.
     */
    @NotBlank(message = "content cannot be blank", groups = {SaveValidator.class})
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
    private MultipartFile[] images;

    /**
     * The video URL associated with the post.
     */
    private String videoURL;

    /**
     * The rating of the post.
     */
    private double rating;
}
