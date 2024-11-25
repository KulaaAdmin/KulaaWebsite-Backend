package com.kula.kula_project_backend.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "regions")
@Accessors(chain = true)
public class Regions {
    @Id
    private ObjectId id;

    @Field("region_name")
    private String regionName;
}
