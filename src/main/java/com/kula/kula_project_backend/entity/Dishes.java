package com.kula.kula_project_backend.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Dishes is an entity class that represents a dish in the application.
 * It is annotated as a MongoDB document and is stored in the "dishes" collection.
 */
@Data
@Document(collection = "dishes")
@Accessors(chain = true)
public class Dishes implements Serializable {

    /**
     * The id of the dish. It is the primary key in the "dishes" collection.
     */
    @Id
    private ObjectId id;

    /**
     * The name of the dish.
     */
    @Field("dish_name")
    private String dishName;

    /**
     * The description of the dish.
     */
    @Field("description")
    private String description;

    /**
     * The price of the dish.
     */
    @Field("price")
    private double price;

    /**
     * The id of the restaurant that the dish belongs to.
     */
    @Field("restaurant_id")
    private ObjectId restaurantId;

    /**
     * The date and time when the dish was created.
     */
    @Field("created_at")
    private Date createdAt;

    /**
     * The date and time when the dish was last updated.
     */
    @Field("updated_at")
    private Date updatedAt;

    /**
     * The rating of the dish.
     */
    @Field("rating")
    private double rating;

    /**
     * The number of reviews for the dish.
     */
    @Field("review_count")
    private int reviewCount;

    /**
     * The filename of dish images.
     */
    @Field("images")
    private ArrayList<String> images;

    /**
     * The tags of dish.
     */
    @Field("tags")
    private ArrayList<ObjectId> tags;

    /**
     * The currency of dish price.
     */
    @Field("currency")
    private String currency;

    /**
     * The review of dish.
     */
    @Field("review")
    private ArrayList<String> dish_review;
}
