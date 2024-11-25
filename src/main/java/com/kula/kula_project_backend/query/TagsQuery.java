package com.kula.kula_project_backend.query;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * TagsQuery is a data transfer object that represents a query for tags.
 * It is used when retrieving tags based on certain criteria.
 */
@Data
public class TagsQuery {

    /**
     * The name of the tag.
     * Used to find tags with a specific name.
     */
    private String tagName;

    /**
     * The id of the tag.
     * Used to find a specific tag.
     */
    private ObjectId id;

    /**
     * The URL of the image associated with the tag.
     * Used to find tags with a specific image URL.
     */
    private String imageURL;
}
