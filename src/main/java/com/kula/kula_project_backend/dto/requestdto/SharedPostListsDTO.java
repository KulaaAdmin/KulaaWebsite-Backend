package com.kula.kula_project_backend.dto.requestdto;

import com.kula.kula_project_backend.common.validator.SaveValidator;
import com.kula.kula_project_backend.common.validator.UpdateValidator;
import lombok.Data;
import org.bson.types.ObjectId;

import javax.validation.constraints.NotNull;

/**
 * SharedPostListsDTO is a data transfer object for the SharedPostLists entity.
 * It is used when saving or updating shared post lists.
 */
@Data
public class SharedPostListsDTO {
    /**
     * The id of the shared post list.
     * It is required when updating a shared post list.
     */
    @NotNull(message = "id cannot be null", groups = {UpdateValidator.class})
    private ObjectId id;

    /**
     * The id of the user who owns the shared post list.
     * It is required when saving or updating a shared post list.
     */
    @NotNull(message = "userId cannot be null", groups = {SaveValidator.class, UpdateValidator.class})
    private ObjectId userId;

    /**
     * The name of the collection that the shared post list belongs to.
     */
    private String collectionName;

    /**
     * The id of the post that is included in the shared post list.
     * It is required when saving or updating a shared post list.
     */
    @NotNull(message = "postIds cannot be null", groups = {SaveValidator.class, UpdateValidator.class})
    private ObjectId postId;
}
