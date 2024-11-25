package com.kula.kula_project_backend.controller;

import com.azure.core.annotation.Get;
import com.kula.kula_project_backend.common.Constant;
import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.common.validator.SaveValidator;
import com.kula.kula_project_backend.common.validator.UpdateValidator;
import com.kula.kula_project_backend.dto.requestdto.ProfilesDTO;
import com.kula.kula_project_backend.service.impl.ProfilesServiceImpl;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;
/**
 * ProfilesController is a REST controller that provides endpoints for managing profiles.
 */
@RestController
@RequestMapping("/profiles")
public class ProfilesController {
    @Autowired
    private ProfilesServiceImpl profilesServiceImpl;
    /**
     * Endpoint to save a new profile.
     * @param profilesDTO The profiles data transfer object containing the profile details.
     * @return The result of the save operation.
     */
    @PostMapping("/save")
    public ResponseResult save(@RequestBody @Validated(SaveValidator.class) ProfilesDTO profilesDTO) {
        return profilesServiceImpl.save(profilesDTO);
    }
    /**
     * Endpoint to get a profile by user id.
     * @param userId The id of the user.
     * @return The result of the get operation.
     */
    @GetMapping("/getProfileByUserId/{userId}")
    public ResponseResult getProfileByUserId(@PathVariable("userId") ObjectId userId) {
        return profilesServiceImpl.getProfileByUserId(userId);
    }
    /**
     * Endpoint to update a profile's bio.
     * @param profilesDTO The profiles data transfer object containing the updated profile details.
     * @return The result of the update operation.
     */
    @PostMapping("/updateBio")
    public ResponseResult updateBio(@RequestBody @Validated(UpdateValidator.class) ProfilesDTO profilesDTO) {
        return profilesServiceImpl.updateBio(profilesDTO);
    }
    /**
     * Endpoint to update a profile's image.
     * @param profilesDTO The profiles data transfer object containing the updated profile details.
     * @return The result of the update operation.
     */
    @PostMapping("/updateProfileImage")
    public ResponseResult updateProfileImage(@RequestBody @Validated(UpdateValidator.class) ProfilesDTO profilesDTO) {
        return profilesServiceImpl.updateProfileImage(profilesDTO);
    }
    /**
     * Endpoint to update a profile's levels.
     * @param profilesDTO The profiles data transfer object containing the updated profile details.
     * @return The result of the update operation.
     */
    @PostMapping("/updateProfileLevels")
    public ResponseResult updateProfileLevel(@RequestBody @Validated(UpdateValidator.class) ProfilesDTO profilesDTO) {
        return profilesServiceImpl.updateProfileLevels(profilesDTO);
    }
    /**
     * Endpoint to update a profile's points.
     * @param profilesDTO The profiles data transfer object containing the updated profile details.
     * @return The result of the update operation.
     */
    @PostMapping("/updateProfilePoints")
    public ResponseResult updateProfilePoints(@RequestBody @Validated(UpdateValidator.class) ProfilesDTO profilesDTO) {
        return profilesServiceImpl.updateProfilePoints(profilesDTO);
    }
    /**
     * Endpoint to gain profile points.
     * @param profilesDTO The profiles data transfer object containing the updated profile details.
     * @return The result of the operation.
     */
    @PostMapping("/gainProfilePoints")
    public ResponseResult gainProfilePoints(@RequestBody @Validated(UpdateValidator.class) ProfilesDTO profilesDTO) {
        return profilesServiceImpl.gainProfilePoints(profilesDTO);
    }
    /**
     * Endpoint to update a profile's privacy setting.
     * @param profilesDTO The profiles data transfer object containing the updated profile details.
     * @return The result of the update operation.
     */
    @PostMapping("/updatePrivateProfile")
    public ResponseResult updatePrivateProfile(@RequestBody @Validated(UpdateValidator.class) ProfilesDTO profilesDTO) {
        return profilesServiceImpl.updatePrivateProfile(profilesDTO);
    }
    /**
     * Endpoint to get a user's level by user id.
     * @param userId The id of the user.
     * @return The result of the get operation.
     */
    @GetMapping("/getUserLevelByUserId/{userId}")
    public ResponseResult getUserLevel(@PathVariable("userId") ObjectId userId) {
        return profilesServiceImpl.getUserLevel(userId);
    }
    /**
     * Endpoint to get a profile's points by user id.
     * @param userId The id of the user.
     * @return The result of the get operation.
     */
    @GetMapping("/getProfilePointByUserId/{userId}")
    public ResponseResult getProfilePoints(@PathVariable("userId") ObjectId userId) {
        return profilesServiceImpl.getProfilePoints(userId);
    }
    /**
     * Endpoint to get a profile's image URL by user id.
     * @param userId The id of the user.
     * @return The result of the get operation.
     */
    @GetMapping("/getImageURLByUserId/{userId}")
    public ResponseResult getImageURLByUserId(@PathVariable("userId") ObjectId userId) {
        return profilesServiceImpl.getImageURLByUserId(userId);
    }




}
