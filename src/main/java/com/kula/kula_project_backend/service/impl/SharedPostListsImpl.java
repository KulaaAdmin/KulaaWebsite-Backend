package com.kula.kula_project_backend.service.impl;

import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.dao.PostsRepository;
import com.kula.kula_project_backend.dao.SharedPostListsRepository;
import com.kula.kula_project_backend.dto.requestdto.SharedPostListsDTO;
import com.kula.kula_project_backend.entity.SharedPostLists;
import com.kula.kula_project_backend.service.ISharedPostListsService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service implementation for SharedPostLists operations.
 * This service is used to save, delete and get SharedPostLists.
 * The service uses the SharedPostListsRepository to interact with the database.
 * The service uses the PostsRepository to interact with the database.
 * The service uses the SharedPostListsDTO to save the SharedPostLists.
 * The service uses the ResponseResult to return the result of the operation.
 * The service uses the ObjectId to get the ID of the SharedPostLists.
 * The service uses the SharedPostListsResponseDTO to convert SharedPostLists to SharedPostListsResponseDTO.
 * The service uses the SharedPostListsDTO to save the SharedPostLists.
 * The service uses the ObjectId to get the ID of the SharedPostLists.
 */
@Service
public class SharedPostListsImpl implements ISharedPostListsService {

    @Autowired
    private SharedPostListsRepository sharedPostListsRepository;

    @Autowired
    private PostsRepository postsRepository;
    /**
     * Saves a new shared post list for a user.
     * @param userId The ID of the user.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult saveSharedPostLists(ObjectId userId) {
        SharedPostLists sharedPostLists = new SharedPostLists();
        sharedPostLists.setUserId(userId);
        sharedPostLists.setCollectionName("sharedPostLists");
        sharedPostLists.setPostIds(new ObjectId[0]);
        sharedPostListsRepository.insert(sharedPostLists);
        if (sharedPostLists.getId() != null) {
            return new ResponseResult(200, "success", sharedPostLists.getId().toString());
        }
        return new ResponseResult(400, "fail");

    }
    /**
     * Adds a post to a user's shared post list.
     * @param sharedPostListsDTO The DTO containing the shared post list and post IDs.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult addPostToSharedPostLists(SharedPostListsDTO sharedPostListsDTO) {
        return null;
    }
    /**
     * Deletes a post from a user's shared post list.
     * @param sharedPostListsDTO The DTO containing the shared post list and post IDs.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult deletePostFromSharedPostLists(SharedPostListsDTO sharedPostListsDTO) {
        return null;
    }
    /**
     * Retrieves a user's shared post list by their user ID.
     * @param userId The ID of the user.
     * @return ResponseResult object containing status, message and the user's shared post list.
     */
    @Override
    public ResponseResult getSharedPostListsByUserId(ObjectId userId) {
        return null;
    }
}
