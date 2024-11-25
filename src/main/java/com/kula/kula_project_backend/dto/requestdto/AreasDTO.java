package com.kula.kula_project_backend.dto.requestdto;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.web.multipart.MultipartFile;

/**
 * AreasDTO is a data transfer object for the Areas entity.
 * It is used when saving or updating Areas.
 */

@Data
public class AreasDTO {
    /**
     * The id of the area.
     */
    private ObjectId id;

    /**
     * The name of the area.
     */
    private String areaName;

    /**
     * The name of the region.
     */
    private String region;

    /**
     * The representative image of area
     */
    private MultipartFile image;

    /**
     * The descriptive texts of area
     */
    private String content;
}
