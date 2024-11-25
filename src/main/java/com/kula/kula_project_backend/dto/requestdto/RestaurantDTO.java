package com.kula.kula_project_backend.dto.requestdto;

import com.kula.kula_project_backend.common.validator.SaveValidator;
import com.kula.kula_project_backend.common.validator.UpdateValidator;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Map;

/**
 * RestaurantDTO is a data transfer object for the Restaurant entity.
 * It is used when saving or updating restaurants.
 */
@Data
public class RestaurantDTO {
    /**
     * The id of the restaurant.
     * It is required when updating a restaurant.
     */
    @NotNull(message = "id cannot be null", groups = {UpdateValidator.class})
    private ObjectId id;

    /**
     * The name of the restaurant.
     * It is required when saving a restaurant.
     */
    @NotBlank(message = "name cannot be blank", groups = {SaveValidator.class})
    private String name;

    /**
     * The address of the restaurant.
     * It is required when saving a restaurant.
     */
    @NotBlank(message = "address cannot be blank", groups = {SaveValidator.class})
    private String address;

    /**
     * The email of the restaurant.
     * It is required when saving a restaurant.
     */
    @NotBlank(message = "email cannot be blank", groups = {SaveValidator.class})
    private String email;

    /**
     * The phone number of the restaurant.
     * It is required when saving a restaurant.
     */
    @NotNull(message = "phone cannot be null", groups = {SaveValidator.class})
    private long phone;

    /**
     * The opening hours of the restaurant.
     * It is required when saving a restaurant.
     */
    @NotBlank(message = "openingHours cannot be blank", groups = {SaveValidator.class})
    private ArrayList<String> openingHours;

    /**
     * The region of the restaurant.
     */
    @NotNull(message = "region cannot be null", groups = {SaveValidator.class})
    private ObjectId region;

    /**
     * The location of the restaurant.
     */
    private Map<String, String> location;

    /**
     * The area of the restaurant.
     */
    @NotNull(message = "area cannot be null", groups = {SaveValidator.class})
    private ObjectId area;

    /**
     * The description of restaurant
     */
    private String description;

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
     * The bookingLinks of restaurant.
     */
    private ArrayList<String> bookingLinks;

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
