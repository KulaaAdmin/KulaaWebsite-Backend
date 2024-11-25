package com.kula.kula_project_backend.dto.responsedto;

import com.kula.kula_project_backend.common.validator.SaveValidator;
import com.kula.kula_project_backend.common.validator.UpdateValidator;
import lombok.Data;
import org.bson.types.ObjectId;

import javax.validation.constraints.NotNull;

/**
 * ProfilesResponseDTO is a data transfer object for the response of a Profiles entity.
 * It is used when returning a response after saving or updating profiles.
 */
@Data
public class ProfilesResponseDTO {

    /**
     * The id of the profile.
     */
    private String id;

    /**
     * The id of the user who owns the profile.
     */
    private String userId;

    /**
     * The bio of the user.
     */
    private String bio;

    /**
     * The URL of the user's profile image.
     */
    private String profileImageURL;

    // The following fields are commented out but can be used for future development.
    // They represent the user's favorite dish, favorite cuisines, delivery options, and locations.

    // private String favoriteDish;
    // private ObjectId[] favoriteCuisinesIds;
    // private ObjectId[] deliveryOptionsIds;
    // private ObjectId[] locationIds;

    /**
     * The user's level.
     */
    private Integer userLevels;

    /**
     * The user's points.
     */
    private Integer userPoints;

    /**
     * A boolean indicating whether the user's profile is private.
     */
    private boolean privateProfile;

}
