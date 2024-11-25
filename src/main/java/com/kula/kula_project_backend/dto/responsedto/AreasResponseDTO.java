package com.kula.kula_project_backend.dto.responsedto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;

@Accessors(chain = true)
@Data
public class AreasResponseDTO {
    /**
     * The id of the area.
     */
    private String id;

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
    private String image;

    /**
     * The descriptive texts of area
     */
    private String content;
}
