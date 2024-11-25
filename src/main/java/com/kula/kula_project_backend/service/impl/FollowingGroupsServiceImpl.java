package com.kula.kula_project_backend.service.impl;

import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.common.converter.FollowingGroupsResponseDTOConverter;
import com.kula.kula_project_backend.dao.FollowingGroupsRepository;
import com.kula.kula_project_backend.dto.requestdto.FollowingGroupsDTO;
import com.kula.kula_project_backend.dto.responsedto.FollowingGroupsResponseDTO;
import com.kula.kula_project_backend.entity.FollowingGroups;
import com.kula.kula_project_backend.service.IFollowingGroupsService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.nio.file.OpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 * Service implementation for FollowingGroups operations.
 * This service is used to save, add, delete and get FollowingGroups.
 * The service uses the FollowingGroupsRepository to interact with the database.
 * The service uses the FollowingGroupsDTO to save the FollowingGroups.
 * The service uses the FollowingGroupsResponseDTO to convert FollowingGroups to FollowingGroupsResponseDTO.
 * The service uses the ResponseResult to return the result of the operation.
 * The service uses the ObjectId to get the ID of the FollowingGroups.
 */
@Service
public class FollowingGroupsServiceImpl implements IFollowingGroupsService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private FollowingGroupsRepository followingGroupsRepository;
    /**
     * Saves a new following group.
     * @param followingGroupsDTO The DTO containing the following group details.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult save(FollowingGroupsDTO followingGroupsDTO) {
        FollowingGroups followingGroups = new FollowingGroups();
        followingGroups.setOwnerId(followingGroupsDTO.getOwnerId());
        followingGroups.setUserIds(new ObjectId[0]);
        followingGroupsRepository.insert(followingGroups);
        if (followingGroups.getId() != null) {
            return new ResponseResult(200, "success", followingGroups.getId().toString());
        } else {
            return new ResponseResult(400, "fail");
        }

    }
    /**
     * Adds more followings to a group.
     * @param followingGroupsDTO The DTO containing the following group details.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult moreFollowings(FollowingGroupsDTO followingGroupsDTO) {
        FollowingGroups followingGroups = followingGroupsRepository.findById(followingGroupsDTO.getId()).get();
        ObjectId[] userIds = followingGroups.getUserIds();
        ObjectId[] newUserIds = new ObjectId[userIds.length + 1];
        for (int i = 0; i < userIds.length; i++) {
            newUserIds[i] = userIds[i];
        }
        newUserIds[userIds.length] = followingGroupsDTO.getFollowerId();
        followingGroups.setUserIds(newUserIds);
        followingGroupsRepository.save(followingGroups);

        if (followingGroups.getId() != null) {
            return new ResponseResult(200, "success", followingGroups.getId().toString());
        } else {
            return new ResponseResult(400, "fail");
        }
    }
    /**
     * Deletes followings from a group.
     * @param followingGroupsDTO The DTO containing the following group details.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult deleteFollowings(FollowingGroupsDTO followingGroupsDTO) {
        FollowingGroups followingGroups = followingGroupsRepository.findById(followingGroupsDTO.getId()).get();
        ObjectId[] userIds = followingGroups.getUserIds();
        ObjectId[] newUserIds = new ObjectId[userIds.length - 1];
        int j = 0;
        for (int i = 0; i < userIds.length; i++) {
            if (!userIds[i].equals(followingGroupsDTO.getFollowerId())) {
                newUserIds[j] = userIds[i];
                j++;
            }
        }
        followingGroups.setUserIds(newUserIds);
        followingGroupsRepository.save(followingGroups);

        if (followingGroups.getId() != null) {
            return new ResponseResult(200, "success", followingGroups.getId().toString());
        } else {
            return new ResponseResult(400, "fail");
        }
    }
    /**
     * Retrieves followings by owner ID.
     * @param ownerId The ID of the owner.
     * @return ResponseResult object containing status, message and the followings.
     */
    @Override
    public ResponseResult getFollowingsByOwnerId(ObjectId ownerId) {
        Optional<FollowingGroups> followingGroups = followingGroupsRepository.findByOwnerId(ownerId);
        if (followingGroups.isPresent()) {
            FollowingGroups currentFollowingGroups = followingGroups.get();
            FollowingGroupsResponseDTO followingGroupsDTO = FollowingGroupsResponseDTOConverter.convertToResponseDTO(currentFollowingGroups);

            return new ResponseResult(200, "success", followingGroupsDTO);
        } else {
            return new ResponseResult(400, "fail");
        }
    }
    /**
     * Retrieves followings by ID.
     * @param id The ID of the followings.
     * @return ResponseResult object containing status, message and the followings.
     */
    @Override
    public ResponseResult getFollowingsById(ObjectId id){
        Optional<FollowingGroups> followingGroups = followingGroupsRepository.findById(id);
        if (followingGroups.isPresent()) {
            FollowingGroups currentFollowingGroups = followingGroups.get();
            FollowingGroupsResponseDTO followingGroupsDTO = FollowingGroupsResponseDTOConverter.convertToResponseDTO(currentFollowingGroups);

            return new ResponseResult(200, "success", followingGroupsDTO);
        } else {
            return new ResponseResult(400, "fail");
        }
    }
    /**
     * Retrieves followers.
     * @param id The ID of the followers.
     * @return ResponseResult object containing status, message and the followers.
     */
    @Override
    public ResponseResult getFollowers(ObjectId id) {

        Criteria criteria = new Criteria();
        criteria.and("user_ids").in(id);
        Query query = new Query(criteria);
        List<FollowingGroups> followingGroups = mongoTemplate.find(query, FollowingGroups.class);
        List<String> ownerIds = new ArrayList<String>();
        for (FollowingGroups followingGroup : followingGroups) {
            ownerIds.add(followingGroup.getOwnerId().toString());
        }
        return new ResponseResult(200, "success", ownerIds);
    }
}
