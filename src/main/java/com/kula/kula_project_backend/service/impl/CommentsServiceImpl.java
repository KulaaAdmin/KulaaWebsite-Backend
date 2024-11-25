package com.kula.kula_project_backend.service.impl;


import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.dao.CommentsRepository;
import com.kula.kula_project_backend.dao.PostsRepository;
import com.kula.kula_project_backend.dto.requestdto.CommentsDTO;
import com.kula.kula_project_backend.dto.responsedto.CommentsResponseDTO;
import com.kula.kula_project_backend.entity.Comments;
import com.kula.kula_project_backend.entity.Posts;
import com.kula.kula_project_backend.service.ICommentsService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
/**
 * Service implementation for Comments operations.
 * This service is used to save, delete and get Comments.
 * The service uses the CommentsRepository to interact with the database.
 * The service uses the PostsRepository to interact with the database.
 * The service uses the AzureBlobServiceImpl to interact with Azure Blob Storage.
 * The service uses the CommentsResponseDTO to convert Comments to CommentsResponseDTO.
 * The service uses the ResponseResult to return the result of the operation.
 * The service uses the CommentsDTO to save the Comments.
 */
@Service
public class CommentsServiceImpl implements ICommentsService {

    @Autowired
    private CommentsRepository commentsRepository;

    @Value("${azure.blob.container-endpoint}")
    private String containerEndpoint;

    @Autowired
    private PostsRepository postRepository;
    /**
     * Retrieves all comments.
     * @return List of all comments.
     */
    @Override
    public List<Comments> getAll() {
        return commentsRepository.findAll();
    }
    /**
     * Saves a new comment.
     * @param commentsDTO The DTO containing the comment details.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult save(CommentsDTO commentsDTO) {
        Optional<Posts> post = postRepository.findById(commentsDTO.getPostId());
        if (post.isPresent()) {
            Comments comments = new Comments();
            comments.setPostId(commentsDTO.getPostId());
            comments.setContent(commentsDTO.getContent());
            comments.setUserId(commentsDTO.getUserId());
            comments.setCreatedAt(new Date());
            comments.setUpdatedAt(new Date());
            commentsRepository.insert(comments);
            if (comments.getId() != null) {
                return new ResponseResult(200, "success", comments.getId().toString());
            }
        }
        return new ResponseResult(400, "fail");

    }
    /**
     * Retrieves all comments for a specific post.
     * @param id The ID of the post.
     * @return ResponseResult object containing status, message and the post's comments.
     */
    @Override
    public ResponseResult getAllPostsComments(ObjectId id) {

        Comments comments = new Comments();
        comments.setPostId(id);
        Example<Comments> example = Example.of(comments);
        List<Comments> commentsList = commentsRepository.findAll(example);
        List<CommentsResponseDTO> responseList = new ArrayList<CommentsResponseDTO>();
        for (Comments comment : commentsList) {
            CommentsResponseDTO commentsResponseDTO = this.convertToResponseDTO(comment);
            responseList.add(commentsResponseDTO);
        }
        return new ResponseResult(200, "success", responseList);
    }
    /**
     * Deletes a comment by its ID.
     * @param id The ID of the comment.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult deleteComments(ObjectId id) {
        Optional<Comments> comments = commentsRepository.findById(id);
        if (comments.isPresent()) {
            commentsRepository.deleteById(id);
            return new ResponseResult(200, "success");
        }
        return new ResponseResult(400, "Comments not found");
    }
    /**
     * Converts a Comment entity to a CommentsResponseDTO.
     * @param comment The Comment entity.
     * @return The converted CommentsResponseDTO.
     */
    public CommentsResponseDTO convertToResponseDTO(Comments comment) {
        CommentsResponseDTO dto = new CommentsResponseDTO();
        dto.setId(comment.getId().toString());
        dto.setPostId(comment.getPostId().toString());
        dto.setUserId(comment.getUserId().toString());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUpdatedAt(comment.getUpdatedAt());
        return dto;
    }
    /**
     * Retrieves the URLs of images from their names.
     * @param imageNames The names of the images.
     * @return Array of image URLs.
     */
    public String[] getImagesURL(String[] imageNames) {
        String[] imageURLs = new String[imageNames.length];
        for (int i = 0; i < imageNames.length; i++) {
            imageURLs[i] = containerEndpoint + imageNames[i];
        }
        return imageURLs;
    }





}

