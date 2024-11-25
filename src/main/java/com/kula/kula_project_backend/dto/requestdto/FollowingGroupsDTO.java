package com.kula.kula_project_backend.dto.requestdto;

import com.kula.kula_project_backend.common.validator.SaveValidator;
import com.kula.kula_project_backend.common.validator.UpdateValidator;
import lombok.Data;
import org.bson.types.ObjectId;

import javax.validation.constraints.NotNull;

/**
 * FollowingGroupsDTO is a data transfer object for the FollowingGroups entity.
 * It is used when saving or updating following groups.
 */
@Data
public class FollowingGroupsDTO {
    /**
     * The id of the following group.
     * It is required when updating a following group.
     */
    @NotNull(message = "id cannot be null", groups = {UpdateValidator.class})
    private ObjectId id;

    /**
     * The id of the user who owns the following group.
     * It is required when saving a following group.
     */
    @NotNull(message = "ownerId cannot be null", groups = {SaveValidator.class})
    private ObjectId ownerId;

    /**
     * The id of the follower in the following group.
     * It is required when updating a following group.
     */
    @NotNull(message = "followerId cannot be null", groups = {UpdateValidator.class})
    private ObjectId followerId;
}
