package com.kula.kula_project_backend.controller;

import com.kula.kula_project_backend.common.Constant;
import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.common.validator.SaveValidator;
import com.kula.kula_project_backend.common.validator.UpdateValidator;
import com.kula.kula_project_backend.dto.requestdto.PostsDTO;
import com.kula.kula_project_backend.entity.Posts;
import com.kula.kula_project_backend.query.PostsQuery;
import com.kula.kula_project_backend.service.IPostsService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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
 * PostsController is a REST controller that provides endpoints for managing posts.
 * This controller is used to save, delete, update, get and search posts.
 * The controller uses the IPostsService to interact with the service layer.
 * The controller uses the PostsDTO to save the posts.
 * The controller uses the ResponseResult to return the result of the operation.
 * The controller uses the ObjectId to get the ID of the posts.
 * The controller uses the RequestBody to get the request body of the request.
 */
@CrossOrigin(origins = "http://172.20.10.6:8080")
@RestController
@RequestMapping("/posts")
public class PostsController {

    @Autowired
    private IPostsService postsService;
    /**
     * Endpoint to get all posts.
     * @return The result of the get operation.
     */
    @GetMapping("/all")
    public ResponseResult getAll() {
        return postsService.getAll();
    }
    /**
     * Endpoint to save a new post.
     * @param postsDTO The posts data transfer object containing the post details.
     * @return The result of the save operation.
     */
    @PostMapping("/save")
    public ResponseResult save(@ModelAttribute @Validated(SaveValidator.class) PostsDTO postsDTO) {
        return postsService.save(postsDTO);

    }
    /**
     * Endpoint to get a post by its id.
     * @param id The id of the post.
     * @return The result of the get operation.
     */
    @GetMapping("/{id}")
    public ResponseResult get(@PathVariable ObjectId id) {
        return postsService.getById(id);
    }
    /**
     * Endpoint to get a list of posts by parameters.
     * @param postsQuery The posts query object containing the query parameters.
     * @return The result of the get operation.
     */
    @PostMapping("/listByParams")
    public ResponseResult listByParams(@RequestBody PostsQuery postsQuery) {
        return postsService.listByParams(postsQuery);
    }
    /**
     * Endpoint to get a list of posts by tag names.
     * @param postsQuery The posts query object containing the query parameters.
     * @return The result of the get operation.
     */
    @PostMapping("/listByTagNames")
    public ResponseResult listByTagNames(@RequestBody PostsQuery postsQuery) {
        return postsService.listByTagNames(postsQuery);
    }
    /**
     * Endpoint to update a post.
     * @param postsDTO The posts data transfer object containing the updated post details.
     * @return The result of the update operation.
     */
    @PostMapping("/update")
    public ResponseResult update(@ModelAttribute @Validated(UpdateValidator.class) PostsDTO postsDTO) {
        return postsService.update(postsDTO);
    }
    /**
     * Endpoint to delete a post by its id.
     * @param id The id of the post.
     * @return The result of the delete operation.
     */
    @DeleteMapping("/deletePost/{id}")
    public ResponseResult deletePost(@PathVariable ObjectId id) {
        return postsService.deletePost(id);
    }
    /**
     * Endpoint to get a list of posts by date.
     * @param postsQuery The posts query object containing the query parameters.
     * @return The result of the get operation.
     */
    @PostMapping("/listByDateAfter")
    public ResponseResult listByDate(@RequestBody PostsQuery postsQuery) {
        return postsService.listByDateAfter(postsQuery);
    }
    /**
     * Endpoint to get a list of posts by start date and end date.
     * @param postsQuery The posts query object containing the query parameters.
     * @return The result of the get operation.
     */
    @PostMapping("/listByStartDateAndEndDate")
    public ResponseResult listByStartDateAndEndDate(@RequestBody PostsQuery postsQuery) {
        return postsService.listByStartDateAndEndDate(postsQuery);
    }
    /**
     * Endpoint to get a list of posts by comments amount.
     * @param amount The amount of comments.
     * @return The result of the get operation.
     */
    @GetMapping("/listByCommentsAmount/{amount}")
    public ResponseResult listByCommentsAmount(@PathVariable Integer amount) {
        return postsService.listByCommentsAmount(amount);
    }
    /**
     * Endpoint to get image URLs by post id.
     * @param postId The id of the post.
     * @return The image URLs of the post.
     */
    @GetMapping("/getImageURLsByPostId/{postId}")
    public String[] getImageURLsByPostId(@PathVariable ObjectId postId) {
        return postsService.getImageURLsByPostId(postId);
    }
    /**
     * Endpoint to get the average likes amount of a post.
     * @param id The id of the post.
     * @return The result of the get operation.
     */
    @GetMapping("/getLikesAmount/{id}")
    public ResponseResult getAverageLikesAmount(@PathVariable ObjectId id) {
        return postsService.getLikesAmount(id);
    }
    /**
     * Endpoint to get posts by user id.
     * @param id The id of the user.
     * @return The result of the get operation.
     */
    @GetMapping("/getPostsByUserId/{id}")
    public ResponseResult getPostsByUserId(@PathVariable ObjectId id) {
        return postsService.getPostsByUserId(id);
    }
    /**
     * Endpoint to get the amount of posts by user id.
     * @param id The id of the user.
     * @return The result of the get operation.
     */
    @GetMapping("/getPostsAmountByUserId/{id}")
    public ResponseResult getPostsAmountByUserId(@PathVariable ObjectId id) {
        return postsService.getPostsAmountByUserId(id);
    }

    /**
     * Endpoints to get feed of posts (ordered by createdAt Desc)
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/feed/{page}")
    public ResponseResult getFeed(
            @PathVariable int page,
            @RequestParam(defaultValue = "5") int pageSize) {
        if (page < 1) {
            return new ResponseResult(404, "page number starts from 1");
        }
        return postsService.getFeed(page - 1, pageSize);
    }







}
