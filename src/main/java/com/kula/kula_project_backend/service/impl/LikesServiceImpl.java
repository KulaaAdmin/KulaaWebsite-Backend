package com.kula.kula_project_backend.service.impl;

import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.dao.LikesRepository;
import com.kula.kula_project_backend.dto.requestdto.LikesDTO;
import com.kula.kula_project_backend.dto.responsedto.LikesResponseDTO;
import com.kula.kula_project_backend.entity.Likes;
import com.kula.kula_project_backend.service.ILikesService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
/**
 * Service implementation for Likes operations.
 * This service is used to save and get Likes.
 * The service uses the LikesRepository to interact with the database.
 * The service uses the LikesDTO to save the Likes.
 * The service uses the LikesResponseDTO to convert Likes to LikesResponseDTO.
 * The service uses the ResponseResult to return the result of the operation.
 * The service uses the ObjectId to get the ID of the Likes.
 */
@Service
public class LikesServiceImpl implements ILikesService {

    @Autowired
    private LikesRepository likesRepository;
    /**
     * Retrieves all likes.
     * @return ResponseResult object containing status, message and all likes.
     */
    @Override
    public ResponseResult getAll() {
        return new ResponseResult(200, "success", likesRepository.findAll());
    }
    /**
     * Saves a new like or updates an existing one.
     * @param likesDTO The DTO containing the like details.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult save(LikesDTO likesDTO) {
        // The following code is the business logic for saving likes
        // The code will first check if the likes for the target post or comment already exists
        // If it does, the code will then check if the user has already liked the post or comment
        // If the user has not liked the post or comment, the user's id will be added to the list of user ids who have liked the post or comment
        // If the user has already liked the post or comment, the user's id will be removed from the list of user ids who have liked the post or comment
        // If the likes for the target post or comment does not exist, a new likes object will be created and the user's id will be added to the list of user ids who have liked the post or comment
        Optional<Likes> likesOptional = likesRepository.findById(likesDTO.getTargetId());
        if (likesOptional.isPresent()) {
            Likes oldLikes = likesOptional.get();
            ObjectId[] userIds = oldLikes.getUserIds();
            List<ObjectId> userIdsList = new ArrayList<ObjectId>(Arrays.asList(userIds));
            if (!userIdsList.contains(likesDTO.getCurrentUserId())) {
                userIdsList.add(likesDTO.getCurrentUserId());
                userIds = userIdsList.toArray(new ObjectId[0]);
                oldLikes.setUserIds(userIds);
                likesRepository.save(oldLikes);
                return new ResponseResult(200, "success", oldLikes.getTargetId().toString());
            } else {
                userIdsList.remove(likesDTO.getCurrentUserId());
                userIds = userIdsList.toArray(new ObjectId[0]);
                oldLikes.setUserIds(userIds);
                likesRepository.save(oldLikes);
                return new ResponseResult(200, "success", oldLikes.getTargetId().toString());
            }
        } else {
            Likes likes = new Likes();
            likes.setTargetId(likesDTO.getTargetId());
            likes.setTargetType(likesDTO.getTargetType());
            ObjectId[] userIds = {likesDTO.getCurrentUserId()};
            likes.setUserIds(userIds);
            likesRepository.insert(likes);
            if (likes.getTargetId() != null) {
                return new ResponseResult(200, "success", likes.getTargetId().toString());
            }
            return new ResponseResult(400, "fail");
        }
    }
    /**
     * Retrieves likes by target ID and type.
     * @param postId The ID of the post.
     * @param targetType The type of the target.
     * @return ResponseResult object containing status, message and the likes.
     */
    @Override
    public ResponseResult getLikesByTargetIdAndType(ObjectId postId, String targetType) {
        Optional<Likes> likesOptional = likesRepository.findByTargetIdAndTargetType(postId, targetType);
        if (likesOptional.isPresent()) {
            Likes likes = likesOptional.get();
            LikesResponseDTO likesResponseDTO = this.convertToResponseDTO(likes);
            return new ResponseResult(200, "success", likesResponseDTO);


        }
        return new ResponseResult(400, "fail");
    }
    /**
     * Converts a Like entity to a LikesResponseDTO.
     * @param likes The Like entity.
     * @return The converted LikesResponseDTO.
     */
    public LikesResponseDTO convertToResponseDTO(Likes likes) {
        LikesResponseDTO dto = new LikesResponseDTO();
        dto.setTargetId(likes.getTargetId().toString());
        dto.setTargetType(likes.getTargetType().toString());
        ObjectId[] likedUserIds = likes.getUserIds();
        for (int i = 0; i < likes.getUserIds().length; i++) {
            dto.getUserIds().add(likedUserIds[i].toString());
        }
        return dto;
    }


}
