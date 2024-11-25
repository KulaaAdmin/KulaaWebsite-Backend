package com.kula.kula_project_backend.service;

import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.dto.requestdto.FollowingGroupsDTO;
import org.bson.types.ObjectId;

public interface IFollowingGroupsService {
    ResponseResult save(FollowingGroupsDTO followingGroupsDTO);

    ResponseResult moreFollowings(FollowingGroupsDTO followingGroupsDTO);

    ResponseResult deleteFollowings(FollowingGroupsDTO followingGroupsDTO);

    ResponseResult getFollowingsById(ObjectId id);

    ResponseResult getFollowingsByOwnerId(ObjectId ownerId);

    ResponseResult getFollowers(ObjectId id);
}
