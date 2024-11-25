package com.kula.kula_project_backend.controller;

import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.common.validator.SaveValidator;
import com.kula.kula_project_backend.dto.requestdto.LikesDTO;
import com.kula.kula_project_backend.service.ILikesService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
/**
 * LikesController is a REST controller that provides endpoints for managing likes.
 */
@RestController
@RequestMapping("/likes")
public class LikesController {

    @Autowired
    private ILikesService likesService;
    /**
     * Endpoint to save a new like.
     * @param likesDTO The likes data transfer object containing the like details.
     * @return The result of the save operation.
     */
    @PostMapping("/save")
    public ResponseResult save(@RequestBody @Validated(SaveValidator.class) LikesDTO likesDTO) {
        return likesService.save(likesDTO);
    }
    /**
     * Endpoint to get likes by post id and target type.
     * @param targetId The id of the target.
     * @param targetType The type of the target.
     * @return The result of the get operation.
     */
    @GetMapping("/getLikesByPostId/{targetType}/{targetId}")
    public ResponseResult getLikesByPostId(@PathVariable ObjectId targetId, @PathVariable String targetType) {
        return likesService.getLikesByTargetIdAndType(targetId, targetType);
    }


}
