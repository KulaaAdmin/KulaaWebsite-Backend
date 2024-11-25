package com.kula.kula_project_backend.dto.requestdto;

import com.kula.kula_project_backend.common.validator.SaveValidator;
import com.kula.kula_project_backend.common.validator.UpdateValidator;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * ProfilesDTO is a data transfer object for the Profiles entity.
 * It is used when saving or updating profiles.
 */
@Data
public class ProfilesDTO {
    /**
     * The id of the profile.
     * It is required when updating a profile.
     */
    @NotNull(message = "id cannot be null", groups = {UpdateValidator.class})
    private ObjectId id;

    /**
     * The id of the user who owns the profile.
     * It is required when saving a profile.
     */
    @NotNull(message = "userId cannot be null", groups = {SaveValidator.class})
    private ObjectId userId;

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
     * It is required when saving a profile.
     */
    @NotNull(message = "privateProfile cannot be blank", groups = {SaveValidator.class})
    private boolean privateProfile;
}
