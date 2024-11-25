package com.kula.kula_project_backend.controller;

import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.common.validator.SaveValidator;
import com.kula.kula_project_backend.dto.requestdto.CommentsDTO;
import com.kula.kula_project_backend.service.ICommentsService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
/**
 * Controller for handling operations related to comments.
 * This controller is used to save, delete and get comments.
 * The controller uses the ICommentsService to interact with the service layer.
 * The controller uses the CommentsDTO to save the comments.
 * The controller uses the ResponseResult to return the result of the operation.
 * The controller uses the ObjectId to get the ID of the comments.
 * The controller uses the RequestBody to get the request body of the request.
 */
@RestController
@RequestMapping("/comments")
public class CommentsController {
    @Autowired
    private ICommentsService commentsService;
    /**
     * Saves a new comment.
     * @param commentsDTO The DTO containing the comment details.
     * @return ResponseResult object containing status and message of the operation.
     */
    @PostMapping("/save")
    public ResponseResult save(@RequestBody @Validated(SaveValidator.class) CommentsDTO commentsDTO) {
        return commentsService.save(commentsDTO);

    }
    /**
     * Retrieves all comments for a specific post.
     * @param id The ID of the post.
     * @return ResponseResult object containing status, message and the post's comments.
     */
    @GetMapping("/getAllPostsComments/{id}")
    public ResponseResult getALlPostsComments(@PathVariable("id") ObjectId id) {
        return commentsService.getAllPostsComments(id);
    }
    /**
     * Deletes a comment by its ID.
     * @param id The ID of the comment.
     * @return ResponseResult object containing status and message of the operation.
     */
    @DeleteMapping("/deleteComment/{id}")
    public ResponseResult deleteComments(@PathVariable("id") ObjectId id) {
        return commentsService.deleteComments(id);
    }


}
