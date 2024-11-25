package com.kula.kula_project_backend.dto.requestdto;

import com.kula.kula_project_backend.common.validator.SaveValidator;
import com.kula.kula_project_backend.common.validator.UpdateValidator;
import lombok.Data;
import org.bson.types.ObjectId;

import javax.validation.constraints.NotBlank;

/**
 * TagsDTO is a data transfer object for the Tags entity.
 * It is used when saving or updating tags.
 */
@Data
public class TagsDTO {
    /**
     * The id of the tag.
     * It is required when updating a tag.
     */
    @NotBlank(message = "id cannot be blank", groups = {UpdateValidator.class})
    private ObjectId id;

    /**
     * The name of the tag.
     * It is required when saving a tag.
     */
    @NotBlank(message = "tagName cannot be blank", groups = {SaveValidator.class})
    private String tagName;

    /**
     * The URL of the image associated with the tag.
     */
    private String imageURL;
}
