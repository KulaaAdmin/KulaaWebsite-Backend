package com.kula.kula_project_backend.controller;

import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.dto.requestdto.FollowingGroupsDTO;
import com.kula.kula_project_backend.service.IFollowingGroupsService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
/**
 * FollowingGroupsController is a REST controller that provides endpoints for managing following groups.
 * This controller is used to save, delete and get following groups.
 * The controller uses the IFollowingGroupsService to interact with the service layer.
 * The controller uses the FollowingGroupsDTO to save the following groups.
 * The controller uses the ResponseResult to return the result of the operation.
 * The controller uses the ObjectId to get the ID of the following groups.
 * The controller uses the RequestBody to get the request body of the request.
 * The controller uses the PathVariable to get the path variable of the request.
 */
@RequestMapping("/followingGroups")
@RestController
public class FollowingGroupsController {
    @Autowired
    private IFollowingGroupsService followingsGroupService;
    /**
     * Endpoint to save a new following group.
     * @param followingGroupsDTO The following groups data transfer object containing the group details.
     * @return The result of the save operation.
     */
    @PostMapping("/save")
    public ResponseResult save(@RequestBody @Validated FollowingGroupsDTO followingGroupsDTO) {
        return followingsGroupService.save(followingGroupsDTO);

    }
    /**
     * Endpoint to add more followings to a group.
     * @param followingGroupsDTO The following groups data transfer object containing the group details.
     * @return The result of the operation.
     */
    @PostMapping("/moreFollowings")
    public ResponseResult moreFollowings(@RequestBody @Validated FollowingGroupsDTO followingGroupsDTO) {
        return followingsGroupService.moreFollowings(followingGroupsDTO);
    }
    /**
     * Endpoint to delete followings from a group.
     * @param followingGroupsDTO The following groups data transfer object containing the group details.
     * @return The result of the delete operation.
     */
    @PostMapping("/deleteFollowings")
    public ResponseResult deleteFollowings(@RequestBody @Validated FollowingGroupsDTO followingGroupsDTO) {
        return followingsGroupService.deleteFollowings(followingGroupsDTO);
    }


    /**
     * Endpoint to get a following group by its id.
     * @param id The id of the following group.
     * @return The result of the get operation.
     */
    @GetMapping("/{id}")
    public ResponseResult getFollowingsById(@PathVariable ObjectId id) {
        return followingsGroupService.getFollowingsById(id);
    }
    /**
     * Endpoint to get followings by the owner's id.
     * @param userId The id of the owner.
     * @return The result of the get operation.
     */
    @GetMapping("/getFollowingsByOwnerId/{userId}")
    public ResponseResult getFollowingsByUserId(@PathVariable ObjectId userId) {
        return followingsGroupService.getFollowingsByOwnerId(userId);
    }
    /**
     * Endpoint to get followers of a user.
     * @param userId The id of the user.
     * @return The result of the get operation.
     */
    @GetMapping("/getFollowers/{userId}")
    public ResponseResult getFollowers(@PathVariable ObjectId userId) {
        return followingsGroupService.getFollowers(userId);
    }
}
