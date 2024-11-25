package com.kula.kula_project_backend.dto.responsedto;

import com.kula.kula_project_backend.common.validator.SaveValidator;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * BookMarksResponseDTO is a data transfer object for the response of a BookMarks entity.
 * It is used when returning a response after saving bookmarks.
 */
@Data
public class BookMarksResponseDTO {

    /**
     * The id of the bookmark.
     * It is required when saving a bookmark.
     */
    @NotNull(message = "id cannot be null", groups = {SaveValidator.class})
    private ObjectId id;

    /**
     * The id of the user who owns the bookmark.
     * It is required when saving a bookmark.
     */
    @NotNull(message = "userId cannot be null", groups = {SaveValidator.class})
    private ObjectId userId;

    /**
     * The name of the collection that the bookmark belongs to.
     * It is required when saving a bookmark.
     */
    @NotBlank(message = "collectionName cannot be blank", groups = {SaveValidator.class})
    private String collectionName;

    /**
     * An array of post ids that are included in the bookmark.
     * It is required when saving a bookmark.
     */
    @NotNull(message = "postIds cannot be null", groups = {SaveValidator.class})
    private ObjectId[] postIds;
}
