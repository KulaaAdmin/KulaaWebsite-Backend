package com.kula.kula_project_backend.controller;

import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.service.ISharedPostListsService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * SharedPostListsController is a REST controller that provides endpoints for managing shared post lists.
 */
@RequestMapping("/sharedPostLists")
@RestController
public class SharedPostListsController {
    @Autowired
    private ISharedPostListsService sharedPostListsService;
    /**
     * Endpoint to save a new shared post list.
     * @param userId The id of the user.
     * @return The result of the save operation.
     */
    @PostMapping("/saveSharedPostLists")
    public ResponseResult saveSharedPostLists(ObjectId userId) {
        return sharedPostListsService.saveSharedPostLists(userId);
    }

    // The following endpoints are commented out but can be used for future development.

    // Endpoint to add a post to a shared post list.
    // @PostMapping("/addPostToSharedPostLists")
    // public ResponseResult addPostToSharedPostLists(ObjectId userId) {
    //     return sharedPostListsService.addPostToSharedPostLists(userId);
    // }

    // Endpoint to delete a post from a shared post list.
    // @PostMapping("/deletePostFromSharedPostLists")
    // public ResponseResult deletePostFromSharedPostLists(ObjectId userId) {
    //     return sharedPostListsService.deletePostFromSharedPostLists(userId);
    // }

    // Endpoint to get a shared post list by user id.
    // @PostMapping("/getSharedPostListsByUserId")
    // public ResponseResult getSharedPostListsByUserId(ObjectId userId) {
    //     return sharedPostListsService.getSharedPostListsByUserId(userId);
    // }


}
