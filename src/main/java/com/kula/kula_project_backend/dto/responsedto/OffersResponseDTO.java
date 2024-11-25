package com.kula.kula_project_backend.dto.responsedto;

import com.kula.kula_project_backend.entity.Dishes;
import com.kula.kula_project_backend.entity.Tags;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Data
public class OffersResponseDTO {
    private String id;

    private String restaurantId;

    private String restaurantName;

    private Map<String, String> location;

    private String offerName;

    private String description;

//    private List<String> dishes = new ArrayList<String>();

    private Double originalPrice;

    private Double discountedPrice;

    private String termsAndConditions;

    private String available;

    private String booking;

    private Double rating;

    //private List<String> tags = new ArrayList<String>();
    private ArrayList<String> tags;

    private String image;

    private String currency;
//    private String startDate;
//
//    private String endDate;

}
