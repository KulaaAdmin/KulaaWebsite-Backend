package com.kula.kula_project_backend.dto.requestdto;

import com.kula.kula_project_backend.common.validator.UpdateValidator;
import lombok.Data;
import org.bson.types.ObjectId;

import javax.validation.constraints.NotNull;

/**
 * RegionsDTO is a data transfer object for the Regions entity.
 * It is used when saving or updating Regions.
 */

@Data
public class RegionsDTO {
    /**
     * The id of the region.
     */
    @NotNull(message = "id cannot be null", groups = {UpdateValidator.class})
    private ObjectId id;

    /**
     * The name of the region.
     */
    private String regionName;
}
