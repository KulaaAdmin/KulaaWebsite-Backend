package com.kula.kula_project_backend.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Users is an entity class that represents a user in the application.
 * It is annotated as a MongoDB document and is stored in the "users"
 * collection.
 */
@Data
@Document(collection = "users")
@Accessors(chain = true)
public class Users {
    /**
     * The id of the user. It is the primary key in the "users" collection.
     */
    @Id
    private ObjectId id;

    /**
     * The id of the profile associated with the user.
     */
    @Field("profile_id")
    private ObjectId profileId;

    /**
     * The id of the bookmarks associated with the user.
     */
    @Field("bookmarks_id")
    private ObjectId bookMarksId;

    /**
     * The id of the following groups associated with the user.
     */
    @Field("following_groups_id")
    private ObjectId followingGroupsId;

    /**
     * The username of the user.
     */
    @Field("username")
    private String username;

    /**
     * The hashed password of the user.
     */
    @Field("password_hash")
    private String passwordHash;

    /**
     * The email of the user. It is indexed for faster searches.
     */
    @Indexed
    @Field("email")
    private String email;

    /**
     * The phone number of the user. It is indexed for faster searches.
     */
    @Indexed
    @Field("phone_number")
    private String phoneNumber;

    /**
     * The first name of the user.
     */
    @Field("first_name")
    private String firstName;

    /**
     * The last name of the user.
     */
    @Field("last_name")
    private String lastName;

    /**
     * The date and time when the user was created.
     */
    @Field("created_at")
    private Date createdAt;

    /**
     * The date and time when the user was last updated.
     */
    @Field("updated_at")
    private Date updatedAt;

    /**
     * The date and time when the user last logged in.
     */
    @Field("last_login")
    private Date lastLogin;

    /**
     * A boolean indicating whether the user is suspended.
     */
    @Field("suspend")
    private boolean suspend;

    /* 
     * each user can only belong to one role
     * user permissions are determined by roleId
     */
    @Field("role_id")
    private ObjectId roleId;
}