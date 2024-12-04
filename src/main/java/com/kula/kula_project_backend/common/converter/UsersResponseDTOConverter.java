package com.kula.kula_project_backend.common.converter;

import com.kula.kula_project_backend.dto.responsedto.UsersResponseDTO;
import com.kula.kula_project_backend.entity.Users;
/**
 * UsersResponseDTOConverter is a utility class that provides a method to convert a Users entity to a UsersResponseDTO.
 */
public class UsersResponseDTOConverter {
    /**
     * Converts a Users entity to a UsersResponseDTO.
     * @param users The Users entity to be converted.
     * @return A UsersResponseDTO that represents the given Users entity.
     */
    public static UsersResponseDTO convertToResponseDTO(Users users) {
        UsersResponseDTO dto = new UsersResponseDTO();
        dto.setId(users.getId().toString());
        dto.setUsername(users.getUsername());
        dto.setPasswordHash(users.getPasswordHash());
        dto.setEmail(users.getEmail());
        dto.setProfileId(users.getProfileId().toString());
        dto.setPhoneNumber(users.getPhoneNumber());
        dto.setFirstName(users.getFirstName());
        dto.setLastName(users.getLastName());
        dto.setAdmin(users.isAdmin());
        dto.setSuspended(users.isSuspend());
        if(users.getBookMarksId()!=null)
        dto.setBookMarksId(users.getBookMarksId().toString());
        else
        dto.setBookMarksId(null);

        return dto;

    }
}
