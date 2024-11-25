package com.kula.kula_project_backend.common.converter;

import com.kula.kula_project_backend.dto.responsedto.FollowingGroupsResponseDTO;
import com.kula.kula_project_backend.entity.FollowingGroups;

/**
 * FollowingGroupsResponseDTOConverter is a utility class that provides a method to convert a FollowingGroups entity to a FollowingGroupsResponseDTO.
 */
public class FollowingGroupsResponseDTOConverter {
    /**
     * Converts a FollowingGroups entity to a FollowingGroupsResponseDTO.
     * @param followingGroups The FollowingGroups entity to be converted.
     * @return A FollowingGroupsResponseDTO that represents the given FollowingGroups entity.
     */
    public static FollowingGroupsResponseDTO convertToResponseDTO(FollowingGroups followingGroups) {
        FollowingGroupsResponseDTO followingGroupsResponseDTO = new FollowingGroupsResponseDTO();
        followingGroupsResponseDTO.setId(followingGroups.getId().toString());
        followingGroupsResponseDTO.setOwnerId(followingGroups.getOwnerId().toString());
        for (int i = 0; i < followingGroups.getUserIds().length; i++) {
            followingGroupsResponseDTO.getUserIds().add(followingGroups.getUserIds()[i].toString());
        }
        return followingGroupsResponseDTO;
    }
}
