package com.kula.kula_project_backend.dto.requestdto;

import com.kula.kula_project_backend.common.validator.SaveValidator;
import com.kula.kula_project_backend.common.validator.UpdateValidator;
import com.kula.kula_project_backend.entity.Dishes;
import com.kula.kula_project_backend.entity.Tags;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class OffersDTO {

    @NotNull(message = "id cannot be null", groups = {UpdateValidator.class})
    private ObjectId id;

    @NotNull(message = "restaurant id cannot be null", groups = {SaveValidator.class})
    private ObjectId restaurantId;

    @NotNull(message = "offer name cannot be null", groups = {SaveValidator.class})
    private String offerName;

//    @NotNull(message = "description cannot be null", groups = {SaveValidator.class})
    private String description;

    @NotNull(message = "dishes cannot be null", groups = {SaveValidator.class})
    private ObjectId[] dishes;

    @NotNull(message = "original price cannot be null", groups = {SaveValidator.class})
    private Double originalPrice;

    @NotNull(message = "discounted price cannot be null", groups = {SaveValidator.class})
    private Double discountedPrice;

    @NotNull(message = "terms and conditions cannot be null", groups = {SaveValidator.class})
    private String termsAndConditions;

    @NotNull(message = "available cannot be null", groups = {SaveValidator.class})
    private String available;

    @NotNull(message = "booking cannot be null", groups = {SaveValidator.class})
    private String booking;

    @NotNull(message = "tags cannot be null", groups = {SaveValidator.class})
    private ArrayList<String> tags;

//    @NotNull(message = "image cannot be null", groups = {SaveValidator.class})
//    private String image;

    @NotNull(message = "start date cannot be null", groups = {SaveValidator.class})
    private String startDate;

    @NotNull(message = "end date cannot be null", groups = {SaveValidator.class})
    private String endDate;
}
