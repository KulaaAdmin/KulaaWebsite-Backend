package com.kula.kula_project_backend.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "offers")
@Accessors(chain = true)
public class Offers implements Serializable {
    @Id
    private ObjectId id;

    @Field("restaurant_id")
    private ObjectId restaurantId;

    @Field("name")
    private String offerName;

    @Field("description")
    private String description;

    @Field("dishes")
    private ObjectId[] dishes;

    @Field("original_price")
    private Double originalPrice;

    @Field("discounted_price")
    private Double discountedPrice;

    @Field("terms_and_conditions")
    private String termsAndConditions;

    @Field("available")
    private String available;

    @Field("booking")
    private String booking;

    @Field("tags")
    private ArrayList<ObjectId> tags;

    @Field("image_file_name")
    private String image;

    @Field("validity_period.start_date")
    private String startDate;

    @Field("validity_period.end_date")
    private String endDate;

    @Field("currency")
    private String currency;
}
