package com.kula.kula_project_backend.dto.responsedto;


import lombok.Data;
import lombok.experimental.Accessors;

/**
 * TagsResponseDTO is a data transfer object for the response of a Tags entity.
 * It is used when returning a response after saving or updating tags.
 */
@Accessors(chain = true)
@Data
public class TagsResponseDTO {
    /**
     * The id of the tag.
     */
    private String id;

    /**
     * The name of the tag.
     */
    private String tagName;

    /**
     * The URL of the image associated with the tag.
     */
    private String imageURL;
}
