package com.kula.kula_project_backend.service.impl;

import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.dao.DietRepository;
import com.kula.kula_project_backend.dto.requestdto.DietsDTO;
import com.kula.kula_project_backend.dto.responsedto.DietsResponseDTO;
import com.kula.kula_project_backend.dto.responsedto.TagsResponseDTO;
import com.kula.kula_project_backend.entity.Diets;
import com.kula.kula_project_backend.entity.Tags;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.kula.kula_project_backend.service.IDietService;
/**
 * Service implementation for Diet operations.
 * This service is used to save, delete and get Diets.
 * The service uses the DietRepository to interact with the database.
 * The service uses the DietsDTO to save the Diets.
 * The service uses the DietsResponseDTO to convert Diets to DietsResponseDTO.
 * The service uses the ResponseResult to return the result of the operation.
 * The service uses the ObjectId to get the ID of the Diet.
 */
@Service
public class DietServiceImpl implements IDietService{
    @Autowired
    private DietRepository dietRepository;
    /**
     * Retrieves all diets.
     * @return List of all diets.
     */
    @Override
    public List<Diets> getAll() {
        return dietRepository.findAll();
    }
    /**
     * Saves a new diet.
     * @param dietDTO The DTO containing the diet details.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult save(DietsDTO dietDTO) {
        Diets diet = new Diets();
        diet.setDietName(dietDTO.getDietName());
        dietRepository.insert(diet);
        if(diet.getId() != null) {
            return new ResponseResult(200, "success", diet.getId());
        }
        return new ResponseResult(400, "fail");
    }
    /**
     * Deletes a diet by its ID.
     * @param id The ID of the diet.
     * @return ResponseResult object containing status and message of the operation.
     */
    @Override
    public ResponseResult deleteDiet(ObjectId id) {
        Optional<Diets> diets = dietRepository.findById(id);
        if(diets.isPresent()) {
            dietRepository.deleteById(id);
            return new ResponseResult(200, "success");
        } else {
            return new ResponseResult(400, "fail");
        }
    }
    /**
     * Retrieves a diet by its name.
     * @param dietName The name of the diet.
     * @return ResponseResult object containing status, message and the diet's ID.
     */
    @Override
    public ResponseResult getDietByName(String dietName) {
        Optional<Diets> diets = dietRepository.findByDietName(dietName); // 假设你有这样的查询方法
        return diets.map(t -> new ResponseResult(200, "success", t.getId().toString()))
                .orElse(new ResponseResult(400, "Tag not found"));
    }
    /**
     * Finds an existing diet by its name or creates a new one if it doesn't exist.
     * @param dietName The name of the diet.
     * @return ResponseResult object containing status, message and the diet's ID.
     */
    @Override
    public ResponseResult findOrCreateDiet(String dietName) {
        if (dietName == null || dietName.trim().isEmpty()) {
            return new ResponseResult(400, "Tag name cannot be blank");
        }

        Optional<Diets> existingDiet = dietRepository.findByDietName(dietName);
        if (existingDiet.isPresent()) {
            return new ResponseResult(200, "Tag found", existingDiet.get().getId().toString());
        } else {
            Diets diets = new Diets();
            diets.setDietName(dietName);
            dietRepository.insert(diets);
            if (diets.getId() != null) {
                return new ResponseResult(200, "Tag created successfully", diets.getId().toString());
            } else {
                return new ResponseResult(500, "Failed to create tag");
            }
        }
    }
    /**
     * Retrieves a limited number of diets.
     * @param limit The number of diets to retrieve.
     * @return ResponseResult object containing status, message and the retrieved diets.
     */
    @Override
    public ResponseResult getLimitedDiets(int limit) {
        List<Diets> allDiets = dietRepository.findAll();
        Collections.shuffle(allDiets);
        List<DietsResponseDTO> randomDietsDTOs = allDiets.stream()
                .limit(limit)
                .map(diet -> new DietsResponseDTO()
                        .setId(diet.getId().toString())
                        .setDietName(diet.getDietName()))
                .collect(Collectors.toList());
        if (!randomDietsDTOs.isEmpty()) {
            return new ResponseResult(200, "success", randomDietsDTOs);
        } else {
            return new ResponseResult(404, "No tags found");
        }
    }
}
