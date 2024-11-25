package com.kula.kula_project_backend.dto.requestdto;

import com.kula.kula_project_backend.common.validator.SaveValidator;
import com.kula.kula_project_backend.common.validator.UpdateValidator;
import lombok.Data;
import org.bson.types.ObjectId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

/**
 * CommentsDTO is a data transfer object for the Comments entity.
 * It is used when saving or updating comments.
 */
@Data
public class CommentsDTO {
    /**
     * The id of the comment.
     * It is required when updating a comment.
     */
    @NotNull(message = "id cannot be null", groups = {UpdateValidator.class})
    private ObjectId id;

    /**
     * The id of the post that the comment is associated with.
     * It is required when saving a comment.
     */
    @NotNull(message = "postId cannot be null", groups = {SaveValidator.class})
    private ObjectId postId;

    /**
     * The content of the comment.
     * It is required when saving a comment.
     */
    @NotBlank(message = "content cannot be blank", groups = {SaveValidator.class})
    private String content;

    /**
     * The id of the user who made the comment.
     * It is required when saving a comment.
     */
    @NotNull(message = "userId cannot be blank", groups = {SaveValidator.class})
    private ObjectId userId;

    /**
     * The date and time when the comment was created.
     */
    private Date createdAt;

    /**
     * The date and time when the comment was last updated.
     */
    private Date updatedAt;
}
