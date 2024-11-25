package com.kula.kula_project_backend.controller;

import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.common.validator.SaveValidator;
import com.kula.kula_project_backend.dto.requestdto.DietsDTO;
import com.kula.kula_project_backend.service.IDietService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
/**
 * Controller for handling operations related to diets.
 * This controller is used to save, delete and get diets.
 * The controller uses the IDietService to interact with the service layer.
 * The controller uses the DietsDTO to save the diets.
 */
//@CrossOrigin(origins = "http://10.12.38.127:8080")
@CrossOrigin(origins = "http://10.12.42.229:8080")
@RequestMapping("/diets")
@RestController
public class DietsController {

    @Autowired
    private IDietService dietsService;
    /**
     * Saves a new diet.
     * @param dietsDTO The DTO containing the diet details.
     * @return ResponseResult object containing status and message of the operation.
     */
    @PostMapping("/save")
    public ResponseResult save(@RequestBody @Validated(SaveValidator.class) DietsDTO dietsDTO) {
        return dietsService.save(dietsDTO);
    }
    /**
     * Deletes a diet by its ID.
     * @param id The ID of the diet.
     * @return ResponseResult object containing status and message of the operation.
     */
    @DeleteMapping("/deleteDiet/{id}")
    public ResponseResult deleteDiets(@PathVariable ObjectId id) {
        return dietsService.deleteDiet(id);
    }
    /**
     * Retrieves a diet by its name.
     * @param dietName The name of the diet.
     * @return ResponseResult object containing status, message and the diet's ID.
     */
    @GetMapping("/getByName/{dietName}")
    public ResponseResult getDietByName(@PathVariable String dietName) {
        return dietsService.getDietByName(dietName);
    }
    /**
     * Retrieves a limited number of diets.
     * @param limit The number of diets to retrieve.
     * @return ResponseResult object containing status, message and the retrieved diets.
     */
    @GetMapping("/getLimitedDiets")
    public ResponseResult getLimitedDiets(@RequestParam int limit) {
        return dietsService.getLimitedDiets(limit);
    }
    /**
     * Finds an existing diet by its name or creates a new one if it doesn't exist.
     * @param dietName The name of the diet.
     * @return ResponseResult object containing status, message and the diet's ID.
     */
    @PostMapping("/findOrCreateDiet")
    public ResponseResult findOrCreateDiet(@RequestBody @Validated(SaveValidator.class) String dietName) {
        return dietsService.findOrCreateDiet(dietName);
    }

}
