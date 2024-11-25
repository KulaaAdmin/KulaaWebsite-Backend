package com.kula.kula_project_backend.dto.responsedto;

import com.kula.kula_project_backend.common.validator.SaveValidator;
import lombok.Data;
import org.bson.types.ObjectId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * SharedPostListResponseDTO is a data transfer object for the response of a SharedPostLists entity.
 * It is used when returning a response after saving or updating shared post lists.
 */
@Data
public class SharedPostListResponseDTO {
    /**
     * The id of the shared post list.
     * It is required when saving a shared post list.
     */
    @NotNull(message = "id cannot be null", groups = {SaveValidator.class})
    private ObjectId id;

    /**
     * The id of the user who owns the shared post list.
     * It is required when saving a shared post list.
     */
    @NotNull(message = "userId cannot be null", groups = {SaveValidator.class})
    private ObjectId userId;

    /**
     * The name of the collection that the shared post list belongs to.
     * It is required when saving a shared post list.
     */
    @NotBlank(message = "collectionName cannot be blank", groups = {SaveValidator.class})
    private String collectionName;

    /**
     * An array of post ids that are included in the shared post list.
     * It is required when saving a shared post list.
     */
    @NotNull(message = "postIds cannot be null", groups = {SaveValidator.class})
    private ObjectId[] postIds;
}
