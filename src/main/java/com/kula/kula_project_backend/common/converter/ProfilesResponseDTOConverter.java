package com.kula.kula_project_backend.common.converter;

import com.kula.kula_project_backend.dto.responsedto.ProfilesResponseDTO;
import com.kula.kula_project_backend.entity.Profiles;
/**
 * ProfilesResponseDTOConverter is a utility class that provides a method to convert a Profiles entity to a ProfilesResponseDTO.
 */
public class ProfilesResponseDTOConverter {
    /**
     * Converts a Profiles entity to a ProfilesResponseDTO.
     * @param profiles The Profiles entity to be converted.
     * @return A ProfilesResponseDTO that represents the given Profiles entity.
     */
    public static ProfilesResponseDTO convertToResponseDTO(Profiles profiles) {
        ProfilesResponseDTO dto = new ProfilesResponseDTO();
        dto.setId(profiles.getId().toString());
        dto.setUserId(profiles.getUserId().toString());
        dto.setBio(profiles.getBio());
        dto.setProfileImageURL(profiles.getProfileImageURL());
        dto.setUserLevels(profiles.getUserLevels());
        dto.setUserPoints(profiles.getUserPoints());
        dto.setPrivateProfile(profiles.isPrivateProfile());
        return dto;

    }
}
