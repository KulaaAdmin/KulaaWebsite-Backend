package com.kula.kula_project_backend.controller;

import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.dto.requestdto.BookMarksDTO;
import com.kula.kula_project_backend.service.IBookMarksService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
/**
 * Controller for handling operations related to bookmarks.
 * This controller is used to save, add, delete and get bookmarks.
 * The controller uses the IBookMarksService to interact with the service layer.
 * The controller uses the BookMarksDTO to save the bookmarks.
 * The controller uses the ResponseResult to return the result of the operation.
 * The controller uses the ObjectId to get the ID of the bookmarks.
 * The controller uses the RequestBody to get the request body of the request.
 * The controller uses the PathVariable to get the path variable of the request.
 */
@RestController
@RequestMapping("/bookmarker")
public class BookMarkersController {

    @Autowired
    private IBookMarksService bookMarksService;
    /**
     * Saves a new bookmark for a user.
     * @param userId The ID of the user.
     * @return ResponseResult object containing status and message of the operation.
     */
    @PostMapping("/save/{userId}")
    public ResponseResult save(ObjectId userId) {
        return bookMarksService.saveBookMarks(userId);
    }
    /**
     * Adds a post to a user's bookmarks.
     * @param bookMarksDTO The DTO containing the bookmark and post IDs.
     * @return ResponseResult object containing status and message of the operation.
     */
    @PostMapping("/addPost")
    public ResponseResult addPost(@RequestBody BookMarksDTO bookMarksDTO) {
        return bookMarksService.addPostToBookMarks(bookMarksDTO);
    }
    /**
     * Deletes a post from a user's bookmarks.
     * @param bookMarksDTO The DTO containing the bookmark and post IDs.
     * @return ResponseResult object containing status and message of the operation.
     */
    @PostMapping("/deletePost")
    public ResponseResult deletePost(@RequestBody BookMarksDTO bookMarksDTO) {
        return bookMarksService.deletePostFromBookMarks(bookMarksDTO);
    }
    /**
     * Retrieves a user's bookmarks by their user ID.
     * @param userId The ID of the user.
     * @return ResponseResult object containing status, message and the user's bookmarked post IDs.
     */
    @GetMapping("/getBookMarksByUserId/{userId}")
    public ResponseResult getBookMarks(@PathVariable ObjectId userId) {
        return bookMarksService.getBookMarksByUserId(userId);
    }
    /**
     * Retrieves a bookmark by its ID.
     * @param id The ID of the bookmark.
     * @return ResponseResult object containing status, message and the bookmarked post IDs.
     */
    @GetMapping("/getBookMarksById/{id}")
    public ResponseResult getBookMarksById(@PathVariable ObjectId id) {
        return bookMarksService.getBookMarksById(id);
    }

}
