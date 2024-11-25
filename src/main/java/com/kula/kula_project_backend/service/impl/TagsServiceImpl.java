package com.kula.kula_project_backend.service.impl;

import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.dao.TagsRepository;
import com.kula.kula_project_backend.dto.requestdto.TagsDTO;
import com.kula.kula_project_backend.dto.responsedto.TagsResponseDTO;
import com.kula.kula_project_backend.entity.Tags;
import com.kula.kula_project_backend.service.ITagsService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service implementation for Tags operations.
 * This service is used to save, delete and get Tags.
 * The service uses the TagsRepository to interact with the database.
 * The service uses the TagsDTO to save the Tags.
 * The service uses the TagsResponseDTO to convert Tags to TagsResponseDTO.
 * The service uses the ResponseResult to return the result of the operation.
 * The service uses the ObjectId to get the ID of the Tags.
 */
@Service
public class TagsServiceImpl implements ITagsService {
    @Autowired
    private TagsRepository tagsRepository;
    /**
     * Retrieves all tags.
     * @return ResponseResult object containing status, message and all tags.
     */
    @Override
    public ResponseResult getAll() {
        return new ResponseResult(200,"success",tagsRepository.findAll());
    }
    /**
     * Saves a new tag or updates an existing one.
     * @param tagsDTO The DTO containing the tag details.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult save(TagsDTO tagsDTO) {
        Tags tags = new Tags();
        tags.setTagName(tagsDTO.getTagName());
        tags.setImageURL(tagsDTO.getImageURL());
        tagsRepository.insert(tags);
        if(tags.getId() != null) {
            return new ResponseResult(200, "success", tags.getId().toString());
        }
        return new ResponseResult(400, "fail");
    }
    /**
     * Deletes a tag by its ID.
     * @param id The ID of the tag.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult deleteTag(ObjectId id) {
        Optional<Tags> tags = tagsRepository.findById(id);
        if(tags.isPresent()) {
            tagsRepository.deleteById(id);
            return new ResponseResult(200, "success");
        } else {
            return new ResponseResult(400, "fail");
        }
    }
    /**
     * Retrieves a tag by its name.
     * @param tagName The name of the tag.
     * @return ResponseResult object containing status, message and the tag's ID.
     */
    @Override
    public ResponseResult getTagByName(String tagName) {
        Optional<Tags> tag = tagsRepository.findByTagName(tagName); // 假设你有这样的查询方法
        return tag.map(t -> new ResponseResult(200, "success", t.getId().toString()))
                .orElse(new ResponseResult(400, "Tag not found"));
    }
    /**
     * Retrieves tag names by their IDs.
     * @param ids The IDs of the tags.
     * @return ResponseResult object containing status, message and the tag names.
     */
    @Override
    public ResponseResult getTagNamesByIds(List<ObjectId> ids) {
        List<Tags> tags = tagsRepository.findByIdIn(ids);
        List<String> tagNames = tags.stream().map(Tags::getTagName).collect(Collectors.toList());
        return new ResponseResult(200, "success", tagNames);
    }
    /**
     * Retrieves a limited number of tags.
     * @param limit The number of tags to retrieve.
     * @return ResponseResult object containing status, message and the retrieved tags.
     */
    @Override
    public ResponseResult getLimitedTags(int limit) {
        List<Tags> allTags = tagsRepository.findAll();
        Collections.shuffle(allTags);
        List<TagsResponseDTO> randomTagsDTOs = allTags.stream()
                .limit(limit)
                .map(tag -> new TagsResponseDTO()
                        .setId(tag.getId().toString())
                        .setTagName(tag.getTagName())
                        .setImageURL(tag.getImageURL()))
                .collect(Collectors.toList());
        if (!randomTagsDTOs.isEmpty()) {
            return new ResponseResult(200, "success", randomTagsDTOs);
        } else {
            return new ResponseResult(404, "No tags found");
        }
    }
    /**
     * Finds an existing tag by its name or creates a new one if it doesn't exist.
     * @param tagName The name of the tag.
     * @return ResponseResult object containing status, message and the tag's ID.
     */
    @Override
    public ResponseResult findOrCreateTag(String tagName) {
        if (tagName == null || tagName.trim().isEmpty()) {
            return new ResponseResult(400, "Tag name cannot be blank");
        }

        Optional<Tags> existingTag = tagsRepository.findByTagName(tagName);
        if (existingTag.isPresent()) {
            return new ResponseResult(200, "Tag found", existingTag.get().getId().toString());
        } else {
            Tags newTag = new Tags();
            newTag.setTagName(tagName);
            tagsRepository.insert(newTag);
            if (newTag.getId() != null) {
                return new ResponseResult(200, "Tag created successfully", newTag.getId().toString());
            } else {
                return new ResponseResult(500, "Failed to create tag");
            }
        }
    }
    @Override
    public ResponseResult searchTags(String name) {
        List<Tags> tags = tagsRepository.findByTagNameContaining(name);
        if (tags.isEmpty()) {
            return new ResponseResult(404, "No tags found");
        } else {
            List<TagsResponseDTO> tagsDTOs = tags.stream()
                    .map(tag -> new TagsResponseDTO()
                            .setId(tag.getId().toString())
                            .setTagName(tag.getTagName())
                            .setImageURL(tag.getImageURL()))
                    .collect(Collectors.toList());
            return new ResponseResult(200, "success", tagsDTOs);
        }
    }

    @Override
    public ResponseResult getOrCreateTags(List<String> tagNames) {
        List<String> ids = tagNames.stream().map(tagName -> {
            Optional<Tags> existingTag = tagsRepository.findByTagName(tagName);
            if (existingTag.isPresent()) {
                return existingTag.get().getId().toString();
            } else {
                Tags newTag = new Tags();
                newTag.setTagName(tagName);
                tagsRepository.insert(newTag);
                return newTag.getId().toString();
            }
        }).collect(Collectors.toList());

        return new ResponseResult(200, "success", ids);
    }


}
