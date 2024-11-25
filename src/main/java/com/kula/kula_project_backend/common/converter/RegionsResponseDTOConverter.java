package com.kula.kula_project_backend.common.converter;

import com.kula.kula_project_backend.dto.responsedto.RegionsResponseDTO;
import com.kula.kula_project_backend.entity.Regions;

public class RegionsResponseDTOConverter {
    /**
     * Converts a Regions entity to a RegionsResponseDTO.
     * @param region The Regions entity to be converted.
     * @return A RegionsResponseDTO that represents the given Regions entity.
     */
    public static RegionsResponseDTO convertToResponseDTO(Regions region) {
        RegionsResponseDTO regionsResponseDTO = new RegionsResponseDTO();
        regionsResponseDTO.setId(region.getId().toString());
        regionsResponseDTO.setRegionName(region.getRegionName());
        return regionsResponseDTO;

    }
}
