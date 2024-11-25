package com.kula.kula_project_backend.dto.requestdto;

import com.kula.kula_project_backend.common.validator.SaveValidator;
import com.kula.kula_project_backend.common.validator.UpdateValidator;
import lombok.Data;
import org.bson.types.ObjectId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * LikesDTO is a data transfer object for the Likes entity.
 * It is used when saving or updating likes.
 */
@Data
public class LikesDTO {
    /**
     * The id of the target (e.g., a post or a comment) that the like is associated with.
     * It is required when saving or updating a like.
     */
    @NotNull(message = "targetId cannot be null", groups = {UpdateValidator.class, SaveValidator.class})
    private ObjectId targetId;

    /**
     * The type of the target that the like is associated with (e.g., "post" or "comment").
     * It is required when saving a like.
     */
    @NotBlank(message = "targetType cannot be blank", groups = {SaveValidator.class})
    private String targetType;

    /**
     * The id of the user who made the like.
     * It is required when saving or updating a like.
     */
    @NotNull(message = "currentUserId cannot be null", groups = {UpdateValidator.class, SaveValidator.class})
    private ObjectId currentUserId;

    // The following field is commented out but can be used for future development.
    // It represents an array of user ids who have liked the target.

    // @NotBlank(message = "userId cannot be blank", groups = {SaveValidator.class})
    // private ObjectId[] userId;
}
