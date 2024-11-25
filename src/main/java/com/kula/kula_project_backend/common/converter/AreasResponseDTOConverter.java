package com.kula.kula_project_backend.common.converter;

import com.kula.kula_project_backend.dto.responsedto.AreasResponseDTO;
import com.kula.kula_project_backend.entity.Areas;


public class AreasResponseDTOConverter {
    /**
     * Converts a Areas entity to a AreasResponseDTO.
     * @param area The Areas entity to be converted.
     * @return A AreasResponseDTO that represents the given Areas entity.
     */
    public static AreasResponseDTO convertToResponseDTO(Areas area) {
        AreasResponseDTO areasResponseDTO = new AreasResponseDTO();
        areasResponseDTO.setId(area.getId().toString());
        areasResponseDTO.setAreaName(area.getAreaName());
        if (area.getRegion()!=null){areasResponseDTO.setRegion(area.getRegion().toString());}
        areasResponseDTO.setContent(area.getContent());
        areasResponseDTO.setImage(area.getImage());
        return areasResponseDTO;
    }
}
