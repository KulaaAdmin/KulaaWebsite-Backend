package com.kula.kula_project_backend.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Profiles is an entity class that represents a user profile in the application.
 * It is annotated as a MongoDB document and is stored in the "profiles" collection.
 */
@Data
@Document(collection = "profiles")
@Accessors(chain = true)
public class Profiles {

    /**
     * The id of the profile. It is the primary key in the "profiles" collection.
     */
    @Id
    private ObjectId id;

    /**
     * The id of the user who owns the profile.
     */
    @Field("user_id")
    private ObjectId userId;

    /**
     * The bio of the user.
     */
    @Field("bio")
    private String bio;

    /**
     * The URL of the user's profile image.
     */
    @Field("profile_image_URL")
    private String profileImageURL;

    /**
     * An array of dish ids that are the user's favorites.
     */
    @Field("favorite_dish_ids")
    private ObjectId[] favoriteDishIds;

    /**
     * An array of cuisine ids that are the user's favorites.
     */
    @Field("favorite_cuisines_ids")
    private ObjectId[] favoriteCuisinesIds;

    /**
     * An array of restaurant ids that are the user's favorites.
     */
    @Field("favorite_restaurants_ids")
    private ObjectId[] deliveryOptionsIds;

    /**
     * The id of the user's location.
     */
    @Field("location_id")
    private ObjectId locationId;

    /**
     * The user's level.
     */
    @Field("user_levels")
    private Integer userLevels;

    /**
     * The user's points.
     */
    @Field("user_points")
    private Integer userPoints;

    /**
     * A boolean indicating whether the user's profile is private.
     */
    @Field("private_profile")
    private boolean privateProfile;
}