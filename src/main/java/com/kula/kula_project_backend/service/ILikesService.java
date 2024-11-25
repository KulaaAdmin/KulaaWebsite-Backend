package com.kula.kula_project_backend.service;

import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.dto.requestdto.LikesDTO;
import org.bson.types.ObjectId;

public interface ILikesService {
    ResponseResult save(LikesDTO likesDTO);

    ResponseResult getAll();

    ResponseResult getLikesByTargetIdAndType(ObjectId postId, String targetType);

}
