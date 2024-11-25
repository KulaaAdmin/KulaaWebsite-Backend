package com.kula.kula_project_backend.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * Restaurant is an entity class that represents a restaurant in the application.
 * It is annotated as a MongoDB document and is stored in the "restaurants" collection.
 */
@Data
@Document(collection = "restaurants")
@Accessors(chain = true)
public class Restaurant implements Serializable {

    /**
     * The id of the restaurant. It is the primary key in the "restaurants" collection.
     */
    @Id
    private ObjectId id;

    /**
     * The name of the restaurant.
     */
    @Field("name")
    private String name;

    /**
     * The address of the restaurant.
     */
    @Field("address")
    private String address;

    /**
     * The description of restaurant
     */
    @Field("description")
    private String description;

    /**
     * The email of the restaurant.
     */
    @Field("email")
    private String email;

    /**
     * The location of the restaurant.
     */
    @Field("location")
    private Map<String, ObjectId> location;

    /**
     * The phone number of the restaurant.
     */
    @Field("phone")
    private long phone;

    /**
     * The opening hours of the restaurant.
     */
    @Field("openingHours")
    private ArrayList<String> openingHours;

    /**
     * The filename of restaurant logo(image).
     */
    @Field("logo")
    private String logo;

    /**
     * The filename of restaurant images.
     */
    @Field("images")
    private ArrayList<String> images;

    /**
     * The tags of restaurant.
     */
    @Field("tags")
    private ArrayList<ObjectId> tags;

    /**
     * The rating of restaurant.
     */
    @Field("rating")
    private Double rating;

    /**
     * The booking links of restaurant.
     */
    @Field("bookingLinks")
    private ArrayList<String> bookingLinks;

    /**
     * The review of restaurant.
     */
    @Field("review")
    private ArrayList<String> restaurant_review;

    /**
     * The zip Code of restaurant
     */
    @Field("zipCode")
    private String zipCode;

    /**
     * The latitude of restaurant
     */
    @Field("latitude")
    private Double latitude;

    /**
     * The longitude of restaurant
     */
    @Field("longitude")
    private Double longitude;

    /**
     * The category of restaurant
     */
    @Field("category")
    private String category;

}
