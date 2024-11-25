package com.kula.kula_project_backend.service;

import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.dto.requestdto.ProfilesDTO;
import org.bson.types.ObjectId;

public interface IProfilesService {
    ResponseResult save(ProfilesDTO profilesDTO);

    ResponseResult getProfileByUserId(ObjectId userId);

    ResponseResult updateBio(ProfilesDTO profilesDTO);

    ResponseResult updateProfileImage(ProfilesDTO profilesDTO);

    ResponseResult updateProfileLevels(ProfilesDTO profilesDTO);

    ResponseResult updateProfilePoints(ProfilesDTO profilesDTO);

    ResponseResult gainProfilePoints(ProfilesDTO profilesDTO);

    ResponseResult updatePrivateProfile(ProfilesDTO profilesDTO);

    ResponseResult getUserLevel(ObjectId userId);

    ResponseResult getProfilePoints(ObjectId userId);

    ResponseResult getImageURLByUserId(ObjectId userId);
}
