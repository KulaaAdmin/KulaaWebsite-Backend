package com.kula.kula_project_backend.service;

import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.dto.requestdto.TagsDTO;
import com.kula.kula_project_backend.entity.Tags;
import org.bson.types.ObjectId;

import java.util.List;

public interface ITagsService {
    ResponseResult getAll();

    ResponseResult save(TagsDTO tagsDTO);

    ResponseResult deleteTag(ObjectId id);
    ResponseResult getTagByName(String tagName); // 新增方法
    ResponseResult getTagNamesByIds(List<ObjectId> ids);
    ResponseResult getLimitedTags(int limit);
    ResponseResult findOrCreateTag(String tagName);
    ResponseResult searchTags(String name);
    ResponseResult getOrCreateTags(List<String> tagNames);



}
