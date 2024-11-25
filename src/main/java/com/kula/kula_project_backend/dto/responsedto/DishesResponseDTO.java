package com.kula.kula_project_backend.dto.responsedto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * DishesResponseDTO is a data transfer object for the response of a Dishes entity.
 * It is used when returning a response after saving or updating dishes.
 */

@Data
public class DishesResponseDTO {
    /**
     * The id of the dish.
     */
    private String id;

    /**
     * The name of the dish.
     */
    private String dishName;

    /**
     * The description of the dish.
     */
    private String description;

    /**
     * The price of the dish.
     */
    private double price;

    /**
     * The id of the restaurant that the dish belongs to.
     */
    private String restaurantId;

    /**
     * The name of the restaurant.
     */
    private String restaurantName;

    /**
     * The logo filename of restaurant.
     */
    private String restaurantLogo;

    /**
     * The address of restaurant.
     */
    private String restaurantAddress;

    private Map<String, String> location;

    /**
     * The date and time when the dish was created.
     */
    private Date createdAt;

    /**
     * The date and time when the dish was last updated.
     */
    private Date updatedAt;

    /**
     * The filename of dish images.
     */
    private ArrayList<String> images;

    /**
     * The tags of dish.
     */
    private ArrayList<String> tags;

    /**
     * The rating of dish.
     */
    private Double rating;

    /**
     * The currency of dish price.
     */
    private String currency;

    /**
     * The review of dish.
     */
    private ArrayList<String> dish_review;
}
