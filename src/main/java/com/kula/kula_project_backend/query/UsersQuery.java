package com.kula.kula_project_backend.query;

import lombok.Data;
import org.bson.types.ObjectId;

/**
 * UsersQuery is a data transfer object that represents a query for users.
 * It is used when retrieving users based on certain criteria.
 */
@Data
public class UsersQuery {
    /**
     * The id of the user.
     * Used to find a specific user.
     */
    private ObjectId userId;

    /**
     * The username of the user.
     * Used to find users with a specific username.
     */
    private String username;

    /**
     * The email of the user.
     * Used to find users with a specific email.
     */
    private String email;

    /**
     * The phone number of the user.
     * Used to find users with a specific phone number.
     */
    private String phoneNumber;

    /**
     * A boolean indicating whether the user is an admin.
     * Used to find users who are admins.
     */
    private boolean admin;

    /**
     * The id of the profile associated with the user.
     * Used to find users with a specific profile.
     */
    private ObjectId profileId;

    /**
     * The first name of the user.
     * Used to find users with a specific first name.
     */
    private String firstName;

    /**
     * The last name of the user.
     * Used to find users with a specific last name.
     */
    private String lastName;

    /**
     * A boolean indicating whether the user is suspended.
     * Used to find users who are suspended.
     */
    private boolean suspended;
}
