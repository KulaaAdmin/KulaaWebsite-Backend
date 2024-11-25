package com.kula.kula_project_backend.dto.responsedto;

import com.kula.kula_project_backend.common.validator.SaveValidator;
import com.kula.kula_project_backend.common.validator.UpdateValidator;
import lombok.Data;
import org.bson.types.ObjectId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * UsersResponseDTO is a data transfer object for the response of a Users entity.
 * It is used when returning a response after saving or updating users.
 */
@Data
public class UsersResponseDTO {

    /**
     * The id of the user.
     */
    private String id;

    /**
     * The username of the user.
     */
    private String username;

    /**
     * The hashed password of the user.
     */
    private String passwordHash;

    /**
     * The email of the user.
     */
    private String email;

    /**
     * The id of the profile associated with the user.
     */
    private String profileId;

    /**
     * The id of the bookmarks associated with the user.
     */
    private String bookMarksId;

    /**
     * The phone number of the user.
     */
    private String phoneNumber;

    /**
     * The first name of the user.
     */
    private String firstName;

    /**
     * The last name of the user.
     */
    private String lastName;

    /**
     * A boolean indicating whether the user is an admin.
     */
    private boolean admin;

    /**
     * A boolean indicating whether the user is suspended.
     */
    private boolean suspended;
}
