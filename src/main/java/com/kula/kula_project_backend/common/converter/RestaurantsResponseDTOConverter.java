package com.kula.kula_project_backend.common.converter;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.kula.kula_project_backend.dao.AreasRepository;
import com.kula.kula_project_backend.dao.RegionsRepository;
import com.kula.kula_project_backend.dao.TagsRepository;
import com.kula.kula_project_backend.dto.responsedto.RestaurantsResponseDTO;
import com.kula.kula_project_backend.entity.Areas;
import com.kula.kula_project_backend.entity.Regions;
import com.kula.kula_project_backend.entity.Restaurant;
import com.kula.kula_project_backend.entity.Tags;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * RestaurantsResponseDTOConverter is a utility class that provides a method to convert a Restaurant entity to a RestaurantsResponseDTO.
 */
//@JsonPropertyOrder({ "region", "area" })
@Component
public class RestaurantsResponseDTOConverter {

    @Autowired
    private TagsRepository tagsRepository;

    @Autowired
    private AreasRepository areasRepository;

    @Autowired
    private RegionsRepository regionsRepository;
    /**
     * Converts a Restaurant entity to a RestaurantsResponseDTO.
     * @param restaurant The Restaurant entity to be converted.
     * @return A RestaurantsResponseDTO that represents the given Restaurant entity.
     */
    public RestaurantsResponseDTO convertToResponseDTO(Restaurant restaurant) {
        RestaurantsResponseDTO dto = new RestaurantsResponseDTO();

        /* transfer tag id to tag name when expose */
        dto.setTags(StreamSupport.stream(tagsRepository.findAllById(restaurant.getTags()).spliterator(), false)
                .map(Tags::getTagName)
                .collect(Collectors.toCollection(ArrayList::new)));

        if(restaurant.getName() != null){
            dto.setName(restaurant.getName());
        }else{
            dto.setName("Unknown Restaurant");
        }

        // Set location
        Optional<Regions> region = Optional.empty();
        Optional<Areas> area = Optional.empty();

        if (restaurant.getLocation().get("region") != null){
            region = regionsRepository.findById(restaurant.getLocation().get("region"));
        }
        if (restaurant.getLocation().get("area") != null){
            area = areasRepository.findById(restaurant.getLocation().get("area"));
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
        dto.setDescription(restaurant.getDescription());
        dto.setId(restaurant.getId().toString());
        dto.setAddress(restaurant.getAddress());
        dto.setEmail(restaurant.getEmail());
        dto.setPhone(restaurant.getPhone());
        dto.setOpeningHours(restaurant.getOpeningHours());
        dto.setLogo(restaurant.getLogo());
        dto.setImages(restaurant.getImages());
        dto.setRestaurant_review(restaurant.getRestaurant_review());
        dto.setZipCode(restaurant.getZipCode());
        dto.setLatitude(restaurant.getLatitude());
        dto.setLongitude(restaurant.getLongitude());
        dto.setCategory(restaurant.getCategory());
        if(restaurant.getRating() != null){
            dto.setRating(restaurant.getRating());
        }else{
            dto.setRating(0.0);
        }
        return dto;
    }
}
