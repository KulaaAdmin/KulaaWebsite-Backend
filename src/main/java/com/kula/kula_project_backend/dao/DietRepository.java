package com.kula.kula_project_backend.dao;


import com.kula.kula_project_backend.entity.Diets;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;
/**
 * DietRepository is a Spring Data MongoDB repository for the Diets entity.
 */
public interface DietRepository extends MongoRepository<Diets, ObjectId> {
    /**
     * Finds a diet by its name.
     * @param dietName The name of the diet.
     * @return An Optional that may contain the Diets object if found.
     */
    @Query("{ 'diet_name' : ?0 }") // 注意匹配数据库字段
    Optional<Diets> findByDietName(String dietName);
    /**
     * Finds diets by their ids.
     * @param ids The list of ids.
     * @return A list of Diets objects that match the given ids.
     */
    List<Diets> findByIdIn(List<ObjectId> ids);
}
