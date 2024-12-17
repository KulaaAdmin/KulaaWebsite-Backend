package com.kula.kula_project_backend.dto.requestdto;

import com.kula.kula_project_backend.common.validator.SaveValidator;
import com.kula.kula_project_backend.common.validator.UpdateValidator;
import com.kula.kula_project_backend.entity.Users;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * UsersDTO is a data transfer object for the Users entity.
 * It is used when saving or updating users.
 */
@Data
@Accessors(chain = true)
public class UsersDTO {
    /**
     * The id of the user.
     * It is required when updating a user.
     */
    @NotNull(message = "id cannot be null", groups = {UpdateValidator.class})
    private ObjectId id;

    /**
     * The username of the user.
     * It is required when saving a user.
     */
    @NotBlank(message = "username cannot be blank", groups = {SaveValidator.class})
    private String username;

    /**
     * The hashed password of the user.
     * It is required when saving a user.
     */
    @NotBlank(message = "passwordHash cannot be blank", groups = {SaveValidator.class})
    private String passwordHash;

    /**
     * The password of the user.
     * It is required when saving a user.
     */
    @NotBlank(message = "password cannot be blank", groups = {SaveValidator.class})
    private String password;

    /**
     * The email of the user.
     */
    private String email;

    /**
     * The registration method of the user.
     * It is required when saving a user.
     */
    @NotBlank(message = "Registration method cannot be blank")
    private String registrationMethod;

    /**
     * The id of the profile associated with the user.
     */
    private ObjectId profileId;

    /**
     * The phone number of the user.
     */
    private String phoneNumber;

    /**
     * The first name of the user.
     * It is required when saving a user.
     */
    @NotBlank(message = "firstName cannot be blank", groups = {SaveValidator.class})
    private String firstName;

    /**
     * The last name of the user.
     * It is required when saving a user.
     */
    @NotBlank(message = "lastName cannot be blank", groups = {SaveValidator.class})
    private String lastName;

    /**
     * TODO: Delete after authentication feature implemented.
     * A boolean indicating whether the user is an admin.
     * It is required when saving or updating a user.
     */
    @NotNull(message = "admin cannot be blank", groups = {SaveValidator.class, UpdateValidator.class})
    private boolean admin;

    /**
     * The verification code of the user.
     */
    private String verificationCode;

    /**
     * A Enum field indicate the role of a user
     * */
    private String roleName;
}
