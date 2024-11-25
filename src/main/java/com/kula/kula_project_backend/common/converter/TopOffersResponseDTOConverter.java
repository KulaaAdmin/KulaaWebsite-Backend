package com.kula.kula_project_backend.common.converter;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.kula.kula_project_backend.dao.AreasRepository;
import com.kula.kula_project_backend.dao.RegionsRepository;
import com.kula.kula_project_backend.dao.TagsRepository;
import com.kula.kula_project_backend.dto.responsedto.TopOffersResponseDTO;
import com.kula.kula_project_backend.entity.*;
import com.kula.kula_project_backend.dao.RestaurantRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@JsonPropertyOrder({ "region", "area" })
@Component
public class TopOffersResponseDTOConverter {

    @Autowired
    private TagsRepository tagsRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private AreasRepository areasRepository;

    @Autowired
    private RegionsRepository regionsRepository;

    public TopOffersResponseDTO convertToResponseDTO(Offers offers) {
        TopOffersResponseDTO dto = new TopOffersResponseDTO();
        dto.setId(offers.getId().toString());
        Optional<Restaurant> restaurant = restaurantRepository.findById(offers.getRestaurantId());
        if (restaurant.isPresent()){
            dto.setRestaurantId(offers.getRestaurantId().toString());
            dto.setRestaurantName(restaurant.get().getName());

            // Set location
            Optional<Regions> region = Optional.empty();
            Optional<Areas> area = Optional.empty();

            if (restaurant.get().getLocation().get("region") != null){
                region = regionsRepository.findById(restaurant.get().getLocation().get("region"));
            }
            if (restaurant.get().getLocation().get("area") != null){
                area = areasRepository.findById(restaurant.get().getLocation().get("area"));
            }

            if (region.isPresent() && area.isPresent()) {
                Map<String, String> location = new HashMap<>();
                location.put("region", region.get().getRegionName());
                location.put("area", area.get().getAreaName());
                dto.setLocation(location);
            } else {
                Map<String, String> location = new HashMap<>();
                location.put("region", "Unknown region" );
                location.put("area", "Unknown area");
                dto.setLocation(location);
            }

            dto.setRating(restaurant.get().getRating());
        }


        /* transfer tag id to tag name when expose */
        ArrayList<String> tagNames = new ArrayList<>();
        for (ObjectId tagId : offers.getTags()){
            Optional<Tags> tag = tagsRepository.findById(tagId);
            tag.ifPresent(tags -> tagNames.add(tags.getTagName()));
        }
        dto.setTags(tagNames);

        dto.setOfferName(offers.getOfferName());
        dto.setOriginalPrice(offers.getOriginalPrice());
        dto.setDiscountedPrice(offers.getDiscountedPrice());
        dto.setImage(offers.getImage());
        return dto;
    }
}
