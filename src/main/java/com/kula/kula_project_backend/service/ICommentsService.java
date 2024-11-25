package com.kula.kula_project_backend.service;

import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.dto.requestdto.CommentsDTO;
import com.kula.kula_project_backend.entity.Comments;
import org.bson.types.ObjectId;

import java.util.List;

public interface ICommentsService {
    List<Comments> getAll();

    ResponseResult save(CommentsDTO commentsDTO);

    ResponseResult getAllPostsComments(ObjectId id);

    ResponseResult deleteComments(ObjectId id);

}
