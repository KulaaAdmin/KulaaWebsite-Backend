package com.kula.kula_project_backend.service.impl;

import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.dao.BookMarksRepository;
import com.kula.kula_project_backend.dao.PostsRepository;
import com.kula.kula_project_backend.dto.requestdto.BookMarksDTO;
import com.kula.kula_project_backend.entity.BookMarks;
import com.kula.kula_project_backend.entity.Posts;
import com.kula.kula_project_backend.service.IBookMarksService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.List;
import java.util.Optional;
/**
 * Service implementation for BookMarks operations.
 * This service is used to save, add, delete and get BookMarks.
 * The service uses the BookMarksRepository to interact with the database.
 * The service uses the PostsRepository to interact with the database.
 */
@Service
public class BookMarksServiceImpl implements IBookMarksService {

    @Autowired
    private BookMarksRepository bookMarksRepository;

    @Autowired
    private PostsRepository postsRepository;
    /**
     * Saves a new bookmark for a user.
     * @param userId The ID of the user.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult saveBookMarks(ObjectId userId) {
        BookMarks bookMarks = new BookMarks();
        bookMarks.setUserId(userId);
        bookMarksRepository.insert(bookMarks);
        if (bookMarks.getId() != null) {
            return new ResponseResult(200, "success", bookMarks.getId().toString());
        }
        return new ResponseResult(400, "fail");
    }
    /**
     * Adds a post to a user's bookmarks.
     * @param bookMarksDTO The DTO containing the bookmark and post IDs.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult addPostToBookMarks(BookMarksDTO bookMarksDTO) {
        Optional<BookMarks> bookMarksOptional = bookMarksRepository.findById(bookMarksDTO.getId());
        if (bookMarksOptional.isPresent()) {
            BookMarks bookMarks = bookMarksOptional.get();
            Optional<Posts> post = postsRepository.findById(bookMarksDTO.getPostId());
            if (post.isPresent()) {
               ObjectId[] postIds = bookMarks.getPostIds();
                if (postIds == null) {
                     postIds = new ObjectId[1];
                     postIds[0] = bookMarksDTO.getPostId();
                     bookMarks.setPostIds(postIds);
                     bookMarksRepository.save(bookMarks);
                     return new ResponseResult(200, "success");
                } else {
                     ObjectId[] newPostIds = new ObjectId[postIds.length + 1];
                     for (int i = 0; i < postIds.length; i++) {
                          newPostIds[i] = postIds[i];
                     }
                     newPostIds[postIds.length] = bookMarksDTO.getPostId();
                     bookMarks.setPostIds(newPostIds);
                     bookMarksRepository.save(bookMarks);
                        return new ResponseResult(200, "success");
                }
            }
        }
        return new ResponseResult(400, "fail");
    }
    /**
     * Deletes a post from a user's bookmarks.
     * @param bookMarksDTO The DTO containing the bookmark and post IDs.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult deletePostFromBookMarks(BookMarksDTO bookMarksDTO) {
        Optional<BookMarks> bookMarksOptional = bookMarksRepository.findById(bookMarksDTO.getId());
        if (bookMarksOptional.isPresent()) {
            BookMarks bookMarks = bookMarksOptional.get();
            ObjectId[] postIds = bookMarks.getPostIds();
            if (postIds != null) {
                ObjectId[] newPostIds = new ObjectId[postIds.length - 1];
                int j = 0;
                for (int i = 0; i < postIds.length; i++) {
                    if (!postIds[i].equals(bookMarksDTO.getPostId())) {
                        newPostIds[j] = postIds[i];
                        j++;
                    }
                }
                bookMarks.setPostIds(newPostIds);
                bookMarksRepository.save(bookMarks);
                return new ResponseResult(200, "success");
            }
        }
        return new ResponseResult(400, "fail");
    }
    /**
     * Retrieves a user's bookmarks by their user ID.
     * @param userId The ID of the user.
     * @return ResponseResult object containing status, message and the user's bookmarked post IDs.
     */
    @Override
    public ResponseResult getBookMarksByUserId(ObjectId userId) {
        Optional<BookMarks> bookMarks = bookMarksRepository.findByUserId(userId);
        if (bookMarks.isPresent()) {
            BookMarks bookMarksEntity = bookMarks.get();
            ObjectId[] postObjectIds = bookMarksEntity.getPostIds();
            String[] postsIds = new String[postObjectIds.length];
            for (int i = 0; i < postObjectIds.length; i++) {
                postsIds[i] = postObjectIds[i].toString();
            }
            return new ResponseResult(200, "success", postsIds);
        }
        return new ResponseResult(400, "fail");
    }
    /**
     * Retrieves a bookmark by its ID.
     * @param id The ID of the bookmark.
     * @return ResponseResult object containing status, message and the bookmarked post IDs.
     */
    @Override
    public ResponseResult getBookMarksById(ObjectId id) {
        Optional<BookMarks> bookMarks = bookMarksRepository.findById(id);
        if (bookMarks.isPresent()) {
            BookMarks bookMarksEntity = bookMarks.get();
            ObjectId[] postObjectIds = bookMarksEntity.getPostIds();
            String[] postsIds = new String[postObjectIds.length];
            for (int i = 0; i < postObjectIds.length; i++) {
                postsIds[i] = postObjectIds[i].toString();
            }
            return new ResponseResult(200, "success", postsIds);
        }
        return new ResponseResult(400, "fail");
    }
}
