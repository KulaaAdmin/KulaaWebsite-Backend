package com.kula.kula_project_backend.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;


/**
 * Diets is an entity class that represents a diet in the application.
 * It is annotated as a MongoDB document and is stored in the "diets" collection.
 */
@Data
@Document(collection = "diets")
@Accessors(chain = true)
public class Diets implements Serializable {

    /**
     * The id of the diet. It is the primary key in the "diets" collection.
     */
    @Id
    private ObjectId id;

    /**
     * The name of the diet.
     */
    @Field("diet_name")
    private String dietName;
}
