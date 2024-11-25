package com.kula.kula_project_backend.dto.requestdto;

import com.kula.kula_project_backend.common.validator.SaveValidator;
import com.kula.kula_project_backend.common.validator.UpdateValidator;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * BookMarksDTO is a data transfer object for the BookMarks entity.
 * It is used when saving or updating bookmarks.
 */
@Data
public class BookMarksDTO {
    /**
     * The id of the bookmark.
     * It is required when updating a bookmark.
     */
    @NotNull(message = "id cannot be null", groups = {UpdateValidator.class})
    private ObjectId id;

    /**
     * The id of the user who owns the bookmark.
     * It is required when saving or updating a bookmark.
     */
    @NotNull(message = "userId cannot be null", groups = {SaveValidator.class, UpdateValidator.class})
    private ObjectId userId;

    /**
     * The name of the collection that the bookmark belongs to.
     */
    private String collectionName;

    /**
     * The id of the post that is included in the bookmark.
     * It is required when saving or updating a bookmark.
     */
    @NotNull(message = "postIds cannot be null", groups = {SaveValidator.class, UpdateValidator.class})
    private ObjectId postId;
}
