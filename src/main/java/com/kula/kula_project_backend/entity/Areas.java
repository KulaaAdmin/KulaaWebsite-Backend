package com.kula.kula_project_backend.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "areas")
@Accessors(chain = true)
public class Areas{
    @Id
    private ObjectId id;

    @Field("area_name")
    private String areaName;

    @Field("region")
    private ObjectId region;

    @Field("image")
    private String image;

    @Field("content")
    private String content;
}
