package com.kula.kula_project_backend.dao;

import com.kula.kula_project_backend.entity.Dishes;
import com.kula.kula_project_backend.entity.Tags;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;
/**
 * TagsRepository is a Spring Data MongoDB repository for the Tags entity.
 */
public interface TagsRepository extends MongoRepository<Tags, ObjectId> {
    /**
     * Finds a tag by its name.
     * @param tagName The name of the tag.
     * @return An Optional that may contain the Tags object if found.
     */
    @Query("{ 'tag_name' : ?0 }")
    Optional<Tags> findByTagName(String tagName);
    /**
     * Finds tags by their ids.
     * @param ids The list of ids.
     * @return A list of Tags objects that match the given ids.
     */
    List<Tags> findByIdIn(List<ObjectId> ids);
    /**
     * Finds tags by their name (case insensitive).
     * @param name The name of the tag.
     * @return A list of Tags objects that match the given name.
     */
    @Query("{ 'tag_name' : { $regex: ?0, $options: 'i' } }")
    List<Tags> findByTagNameContaining(String name);


    /**
     * Finds a tag by its id.
     * @param tagId The id of the tag.
     * @return An Optional that may contain the Tags object if found.
     */
    @Override
    Optional<Tags> findById(ObjectId tagId);
}
