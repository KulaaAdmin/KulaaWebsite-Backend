package com.kula.kula_project_backend.controller;

import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.common.validator.SaveValidator;
import com.kula.kula_project_backend.dto.requestdto.TagsDTO;
import com.kula.kula_project_backend.service.ITagsService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
/**
 * TagsController is a REST controller that provides endpoints for managing tags.
 */
//@CrossOrigin(origins = "http://10.12.38.127:8080")
@CrossOrigin(origins = "http://172.20.10.6:8080")
@RequestMapping("/tags")
@RestController
public class TagsController {

    @Autowired
    private ITagsService tagsService;
    /**
     * Endpoint to save a new tag.
     * @param tagsDTO The tags data transfer object containing the tag details.
     * @return The result of the save operation.
     */
    @PostMapping("/save")
    public ResponseResult save(@RequestBody @Validated(SaveValidator.class) TagsDTO tagsDTO) {
        return tagsService.save(tagsDTO);
    }
    /**
     * Endpoint to delete a tag by its id.
     * @param id The id of the tag.
     * @return The result of the delete operation.
     */
    @DeleteMapping("/deleteTag/{id}")
    public ResponseResult deleteTag(@PathVariable ObjectId id) {
        return tagsService.deleteTag(id);
    }
    /**
     * Endpoint to get a tag by its name.
     * @param tagName The name of the tag.
     * @return The result of the get operation.
     */
    @GetMapping("/getByName/{tagName}")
    public ResponseResult getTagByName(@PathVariable String tagName) {
        return tagsService.getTagByName(tagName);
    }
    /**
     * Endpoint to get tag names by their ids.
     * @param ids The list of ids.
     * @return The result of the get operation.
     */
    @PostMapping("/getNamesByIds")
    public ResponseResult getTagNamesByIds(@RequestBody List<String> ids) {
        List<ObjectId> objectIds = ids.stream().map(ObjectId::new).collect(Collectors.toList());
        return tagsService.getTagNamesByIds(objectIds);
    }
    /**
     * Endpoint to get a limited number of tags.
     * @param limit The limit on the number of tags to return.
     * @return The result of the get operation.
     */
    @GetMapping("/getLimitedTags")
    public ResponseResult getLimitedTags(@RequestParam int limit) {
        return tagsService.getLimitedTags(limit);
    }
    /**
     * Endpoint to find or create a tag by its name.
     * @param tagName The name of the tag.
     * @return The result of the operation.
     */
    @PostMapping("/findOrCreateTag")
    public ResponseResult findOrCreateTag(@RequestBody @Validated(SaveValidator.class) String tagName) {
        return tagsService.findOrCreateTag(tagName);
    }
    /**
     * Endpoint to search for tags by name.
     * @param name The name to search for.
     * @return The result of the search operation.
     */
    @GetMapping("/searchTags")
    public ResponseResult searchTags(@RequestParam String name) {
        return tagsService.searchTags(name);
    }
    /**
     * Endpoint to get or create tags by their names.
     * @param tagNames The list of tag names.
     * @return The result of the operation.
     */
    @PostMapping("/getOrCreateTags")
    public ResponseResult getOrCreateTags(@RequestBody List<String> tagNames) {
        return tagsService.getOrCreateTags(tagNames);
    }
}