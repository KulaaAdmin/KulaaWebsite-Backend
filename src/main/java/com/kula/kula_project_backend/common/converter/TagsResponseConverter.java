package com.kula.kula_project_backend.common.converter;

import com.kula.kula_project_backend.dto.responsedto.TagsResponseDTO;
import com.kula.kula_project_backend.entity.Tags;
/**
 * TagsResponseConverter is a utility class that provides a method to convert a Tags entity to a TagsResponseDTO.
 */
public class TagsResponseConverter {
    /**
     * Converts a Tags entity to a TagsResponseDTO.
     * @param tags The Tags entity to be converted.
     * @return A TagsResponseDTO that represents the given Tags entity.
     */
    public static TagsResponseDTO convertToResponseDTO(Tags tags) {
        TagsResponseDTO tagsResponseDTO = new TagsResponseDTO();
        tagsResponseDTO.setId(tags.getId().toString());
        tagsResponseDTO.setTagName(tags.getTagName());
        tagsResponseDTO.setImageURL(tags.getImageURL());
        return tagsResponseDTO;

    }
}
