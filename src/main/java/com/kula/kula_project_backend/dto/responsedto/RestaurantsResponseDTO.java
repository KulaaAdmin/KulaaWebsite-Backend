package com.kula.kula_project_backend.dto.responsedto;

import java.util.ArrayList;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

/**
 * RestaurantsResponseDTO is a data transfer object for the response of a Restaurant entity.
 * It is used when returning a response after saving or updating restaurants.
 */
@Data
public class RestaurantsResponseDTO {

	/**
	 * The id of the restaurant.
	 */
    private String id;

    /**
     * The name of the restaurant.
     */
    private String name;

    /**
     * The address of the restaurant.
     */
    private String address;

    /**
     * The email of the restaurant.
     */
    private String email;

    /**
     * The phone number of the restaurant.
     */
    private long phone;

    /**
     * The description of restaurant
     */
    private String description;

    /**
     * The opening hours of the restaurant.
     */
    private ArrayList<String> openingHours;

    /**
     * The location of the restaurant.
     */
    private Map<String, String> location;

    /**
     * The filename of restaurant logo(image).
     */
    private String logo;

    /**
     * The filename of restaurant images.
     */
    private ArrayList<String> images;

    /**
     * The tags of restaurant.
     */
    private ArrayList<String> tags;

    /**
     * The rating of restaurant.
     */
    private Double rating;

    /**
     * The booking links of restaurant.
     */
    private ArrayList<String> bookingLinks;

    /**
     * The review of restaurant.
     */
    private ArrayList<String> restaurant_review;

    /**
     * The zip Code of restaurant
     */
    private String zipCode;

    /**
     * The latitude of restaurant
     */
    private Double latitude;

    /**
     * The longitude of restaurant
     */
    private Double longitude;

    /**
     * The category of restaurant
     */
    private String category;

}
