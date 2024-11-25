package com.kula.kula_project_backend.dto.responsedto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * DietsResponseDTO is a data transfer object for the response of a Diets entity.
 * It is used when returning a response after saving or updating diets.
 */
@Accessors(chain = true)
@Data
public class DietsResponseDTO {
        /**
         * The id of the diet.
         */
        private String id;

        /**
         * The name of the diet.
         */
        private String dietName;
}
