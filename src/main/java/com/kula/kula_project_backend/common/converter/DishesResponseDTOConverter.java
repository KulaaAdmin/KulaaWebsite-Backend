package com.kula.kula_project_backend.common.converter;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.kula.kula_project_backend.dao.AreasRepository;
import com.kula.kula_project_backend.dao.RegionsRepository;
import com.kula.kula_project_backend.dao.TagsRepository;
import com.kula.kula_project_backend.dto.responsedto.DishesResponseDTO;
import com.kula.kula_project_backend.entity.*;
import com.kula.kula_project_backend.dao.RestaurantRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * DishesResponseDTOConverter is a utility class that provides a method to convert a Dishes entity to a DishesResponseDTO.
 */
@JsonPropertyOrder({ "region", "area" })
@Component
public class DishesResponseDTOConverter {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private TagsRepository tagsRepository;

    @Autowired
    private AreasRepository areasRepository;

    @Autowired
    private RegionsRepository regionsRepository;
    /**
     * Converts a Dishes entity to a DishesResponseDTO.
     * @param dishes The Dishes entity to be converted.
     * @return A DishesResponseDTO that represents the given Dishes entity.
     */
    public DishesResponseDTO convertToResponseDTO(Dishes dishes) {
        DishesResponseDTO dto = new DishesResponseDTO();
        dto.setId(dishes.getId().toString());
        dto.setRestaurantId(dishes.getRestaurantId().toString());
        Optional<Restaurant> restaurant = restaurantRepository.findById(dishes.getRestaurantId());
        if (restaurant.isPresent()){
            if(restaurant.get().getName() != null){
                dto.setRestaurantName(restaurant.get().getName());
            }else{
                dto.setRestaurantName("Unknown Restaurant");
            }

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

//            Map<String, String> location= new HashMap<>();
//            location.put("region", region.get().getRegionName());
//            location.put("area", area.get().getAreaName());
//            dto.setLocation(location);

            dto.setRestaurantLogo(restaurant.get().getLogo());
            dto.setRestaurantAddress(restaurant.get().getAddress());
        }

        /* transfer tag id to tag name when expose */
        List<ObjectId> tagIds = dishes.getTags();
        dto.setTags(StreamSupport.stream(tagsRepository.findAllById(tagIds).spliterator(), false)
                .map(Tags::getTagName)
                .collect(Collectors.toCollection(ArrayList::new)));

        dto.setCreatedAt(dishes.getCreatedAt());
        dto.setDescription(dishes.getDescription());
        dto.setDishName(dishes.getDishName());
        dto.setPrice(dishes.getPrice());
        dto.setRating(dishes.getRating());
        dto.setRestaurantId(dishes.getRestaurantId().toString());
        dto.setUpdatedAt(dishes.getUpdatedAt());
        dto.setImages(dishes.getImages());
        dto.setRating(dishes.getRating());
        dto.setCurrency(dishes.getCurrency());
        dto.setDish_review(dishes.getDish_review());
        return dto;
    }
}
