package com.kula.kula_project_backend.service;

import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.dto.requestdto.PostsDTO;
import com.kula.kula_project_backend.query.PostsQuery;
import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

public interface IPostsService {
    ResponseResult getAll();

    ResponseResult save(PostsDTO postsDTO);

    ResponseResult getById(ObjectId id);

    ResponseResult listByParams(PostsQuery postsQuery);

    ResponseResult listByTagNames(PostsQuery postsQuery);

    ResponseResult update(PostsDTO postsDTO);

    ResponseResult deletePost(ObjectId id);

    ResponseResult listByDateAfter(PostsQuery postsQuery);

    ResponseResult listByStartDateAndEndDate(PostsQuery postsQuery);

    ResponseResult listByCommentsAmount(Integer amount);

    String[] getImageURLsByPostId(ObjectId id);

    ResponseResult getLikesAmount(ObjectId id);

    ResponseResult getPostsByUserId(ObjectId userId);

    ResponseResult getPostsAmountByUserId(ObjectId userId);

    ResponseResult uploadImagesById(ObjectId id, MultipartFile[] files);

    ResponseResult getFeed(int page, int pageSize);
}
