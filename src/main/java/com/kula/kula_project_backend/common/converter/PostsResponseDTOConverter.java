package com.kula.kula_project_backend.common.converter;

import com.kula.kula_project_backend.dao.TagsRepository;
import com.kula.kula_project_backend.dto.responsedto.PostsResponseDTO;
import com.kula.kula_project_backend.entity.Posts;
import com.kula.kula_project_backend.entity.Tags;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * PostsResponseDTOConverter is a utility class that provides a method to convert a Posts entity to a PostsResponseDTO.
 */

@Component
public class PostsResponseDTOConverter {

    @Autowired
    private TagsRepository tagsRepository;

    /**
     * Converts a Posts entity to a PostsResponseDTO.
     * @param posts The Posts entity to be converted.
     * @return A PostsResponseDTO that represents the given Posts entity.
     */
    public PostsResponseDTO convertToResponseDTO(Posts posts) {

        PostsResponseDTO dto = new PostsResponseDTO();
        dto.setId(posts.getId().toString());
        dto.setAuthId(posts.getAuthId().toString());
        dto.setTitle(posts.getTitle());
        if (posts.getRestaurantId()!=null){
            dto.setRestaurantId(posts.getRestaurantId().toString());
        }
        if (posts.getDishId()!=null){
            dto.setDishId(posts.getDishId().toString());
        }
        /* transfer tag id to tag name when expose */
        ArrayList<String> tagNames = new ArrayList<>();
        if (posts.getTags()!=null){
            for (ObjectId tagId:posts.getTags()){
                if (tagId!= null){  /* Some entries in post database schema contains tags that has been dropped */
                    Optional<Tags> tag = tagsRepository.findById(tagId);
                    tag.ifPresent(tags -> tagNames.add(tags.getTagName()));
                }
            }
        }
        dto.setTags(tagNames);
        dto.setContent(posts.getContent());
        dto.setCreatedAt(posts.getCreatedAt());
        dto.setUpdatedAt(posts.getUpdatedAt());
        dto.setImageURL(posts.getImageURL());
        dto.setImages(posts.getImages());
        dto.setVideoURL(posts.getVideoURL());
        // keep one decimal in rating
        double roundedRating = BigDecimal.valueOf(posts.getRating())
                         .setScale(1, RoundingMode.HALF_UP) 
                         .doubleValue();
        dto.setRating(roundedRating);
        //dto.setRating(posts.getRating());
        return dto;
    }
}
