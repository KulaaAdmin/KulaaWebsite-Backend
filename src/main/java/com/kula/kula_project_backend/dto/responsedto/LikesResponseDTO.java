package com.kula.kula_project_backend.dto.responsedto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * LikesResponseDTO is a data transfer object for the response of a Likes entity.
 * It is used when returning a response after saving or updating likes.
 */
@Data
public class LikesResponseDTO {

    /**
     * The id of the target (e.g., a post or a comment) that the like is associated with.
     */
    private String targetId;

    /**
     * The type of the target that the like is associated with (e.g., "post" or "comment").
     */
    private String targetType;

    /**
     * A list of user ids who have liked the target.
     */
    private List<String> userIds = new ArrayList<String>();

    // The following method is commented out but can be used for future development.
    // It converts a Likes entity to a LikesResponseDTO.

    // public static LikesResponseDTO convertToResponseDTO(Likes likes) {
    //     LikesResponseDTO dto = new LikesResponseDTO();
    //     dto.setTargetId(likes.getTargetId().toString());
    //     dto.setTargetType(likes.getTargetType().toString());
    //     ObjectId[] likedUserIds = likes.getUserIds();
    //     for (int i = 0; i < likes.getUserIds().length; i++) {
    //         dto.getUserIds().add(likedUserIds[i].toString());
    //     }
    //     return dto;
    // }
}
