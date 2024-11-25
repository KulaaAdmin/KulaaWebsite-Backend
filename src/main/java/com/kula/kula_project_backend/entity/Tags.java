package com.kula.kula_project_backend.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * Tags is an entity class that represents a tag in the application.
 * It is annotated as a MongoDB document and is stored in the "tags" collection.
 */
@Data
@Document(collection = "tags")
@Accessors(chain = true)
public class Tags implements Serializable {

    /**
     * The id of the tag. It is the primary key in the "tags" collection.
     */
    @Id
    private ObjectId id;

    /**
     * The name of the tag.
     */
    @Field("tag_name")
    private String tagName;

    /**
     * The URL of the image associated with the tag.
     */
    @Field("image_URL")
    private String imageURL;
}
