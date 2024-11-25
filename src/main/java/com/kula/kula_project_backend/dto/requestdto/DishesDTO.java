package com.kula.kula_project_backend.dto.requestdto;

import com.kula.kula_project_backend.common.validator.SaveValidator;
import com.kula.kula_project_backend.common.validator.UpdateValidator;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;

/**
 * DishesDTO is a data transfer object for the Dishes entity.
 * It is used when saving or updating dishes.
 */
@Data
public class DishesDTO {
    /**
     * The id of the dish.
     * It is required when updating a dish.
     */
    @NotNull(message = "id cannot be null", groups = {UpdateValidator.class})
    private ObjectId id;

    /**
     * The name of the dish.
     * It is required when saving a dish.
     */
    @NotBlank(message = "dishName cannot be blank", groups = {SaveValidator.class})
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
     * It is required when saving a dish.
     */
    @NotNull(message = "restaurantId cannot be blank", groups = {SaveValidator.class})
    private ObjectId restaurantId;

    /**
     * The date and time when the dish was created.
     */
    private Date createdAt;

    /**
     * The date and time when the dish was last updated.
     */
    private Date updatedAt;

    /**
     * The rating of the dish.
     */
    private double rating;

    /**
     * The number of reviews for the dish.
     */
    private int reviewCount;

    /**
     * The filename of dish images.
     */
    private ArrayList<String> images;

    /**
     * The tags of dish.
     */
    private ArrayList<String> tags;
}
