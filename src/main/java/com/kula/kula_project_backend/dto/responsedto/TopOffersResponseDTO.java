package com.kula.kula_project_backend.dto.responsedto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Data
public class TopOffersResponseDTO {
    private String id;

    private String restaurantId;

    private String restaurantName;

    private Map<String, String> location;

    private String offerName;

    private Double originalPrice;

    private Double discountedPrice;

    private Double rating;

    //private List<String> tags = new ArrayList<String>();;
    private ArrayList<String> tags;

    private String image;
}
