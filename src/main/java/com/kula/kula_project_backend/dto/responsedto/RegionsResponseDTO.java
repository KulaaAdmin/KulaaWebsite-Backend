package com.kula.kula_project_backend.dto.responsedto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;

@Accessors(chain = true)
@Data
public class RegionsResponseDTO {
    /**
     * The id of the region.
     */
    private String id;

    /**
     * The name of the region.
     */
    private String regionName;
}
