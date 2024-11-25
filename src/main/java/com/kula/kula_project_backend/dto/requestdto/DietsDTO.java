package com.kula.kula_project_backend.dto.requestdto;

import com.kula.kula_project_backend.common.validator.SaveValidator;
import com.kula.kula_project_backend.common.validator.UpdateValidator;
import lombok.Data;
import org.bson.types.ObjectId;

import javax.validation.constraints.NotBlank;

/**
 * DietsDTO is a data transfer object for the Diets entity.
 * It is used when saving or updating diets.
 */
@Data
public class DietsDTO {
        /**
         * The id of the diet.
         * It is required when updating a diet.
         */
        @NotBlank(message = "id cannot be blank", groups = {UpdateValidator.class})
        private ObjectId id;

        /**
         * The name of the diet.
         * It is required when saving a diet.
         */
        @NotBlank(message = "dietName cannot be blank", groups = {SaveValidator.class})
        private String dietName;
}
