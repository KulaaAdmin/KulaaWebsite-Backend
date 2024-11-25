package com.kula.kula_project_backend.common.converter;

import com.kula.kula_project_backend.dao.RestaurantRepository;
import com.kula.kula_project_backend.dao.TagsRepository;
import com.kula.kula_project_backend.dto.responsedto.OffersResponseDTO;
import com.kula.kula_project_backend.entity.Offers;
import com.kula.kula_project_backend.entity.Restaurant;
import com.kula.kula_project_backend.entity.Tags;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.kula.kula_project_backend.dao.AreasRepository;
import com.kula.kula_project_backend.dao.RegionsRepository;
import com.kula.kula_project_backend.dao.RestaurantRepository;
import com.kula.kula_project_backend.dao.TagsRepository;
import com.kula.kula_project_backend.dto.responsedto.OffersResponseDTO;
import com.kula.kula_project_backend.dto.responsedto.TopOffersResponseDTO;
import com.kula.kula_project_backend.entity.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@JsonPropertyOrder({ "region", "area" })
@Component
public class OffersResponseDTOConverter {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private TagsRepository tagsRepository;

    @Autowired
    private AreasRepository areasRepository;

    @Autowired
    private RegionsRepository regionsRepository;

    public OffersResponseDTO convertToResponseDTO(Offers offers) {

        OffersResponseDTO dto = new OffersResponseDTO();
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
//        System.out.println(offers.getTags());
        dto.setTags(tagNames);

        dto.setOfferName(offers.getOfferName());
        dto.setDescription(offers.getDescription());
        dto.setOriginalPrice(offers.getOriginalPrice());
        dto.setDiscountedPrice(offers.getDiscountedPrice());
        dto.setTermsAndConditions(offers.getTermsAndConditions());
        dto.setAvailable(offers.getAvailable());
        dto.setBooking(offers.getBooking());
        dto.setImage(offers.getImage());
        dto.setCurrency(offers.getCurrency());

        return dto;
    }
}
