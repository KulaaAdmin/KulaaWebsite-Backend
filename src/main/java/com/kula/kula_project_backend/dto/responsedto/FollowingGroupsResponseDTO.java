package com.kula.kula_project_backend.dto.responsedto;

import com.kula.kula_project_backend.common.validator.SaveValidator;
import com.kula.kula_project_backend.common.validator.UpdateValidator;
import lombok.Data;
import org.bson.types.ObjectId;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * FollowingGroupsResponseDTO is a data transfer object for the response of a FollowingGroups entity.
 * It is used when returning a response after saving or updating following groups.
 */
@Data
public class FollowingGroupsResponseDTO {

    /**
     * The id of the following group.
     */
    private String id;

    /**
     * The id of the user who owns the following group.
     */
    private String ownerId;

    /**
     * A list of user ids that are included in the following group.
     */
    private List<String> userIds = new ArrayList<String>();
}
