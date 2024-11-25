package com.kula.kula_project_backend.service;

import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.dto.requestdto.SharedPostListsDTO;
import org.bson.types.ObjectId;

public interface ISharedPostListsService {

    ResponseResult saveSharedPostLists(ObjectId userId);

    ResponseResult addPostToSharedPostLists(SharedPostListsDTO sharedPostListsDTO);

    ResponseResult deletePostFromSharedPostLists(SharedPostListsDTO sharedPostListsDTO);

    ResponseResult getSharedPostListsByUserId(ObjectId userId);
}
