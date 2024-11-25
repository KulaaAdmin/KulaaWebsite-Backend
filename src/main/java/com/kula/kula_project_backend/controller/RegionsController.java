package com.kula.kula_project_backend.controller;

import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.common.validator.SaveValidator;
import com.kula.kula_project_backend.dto.requestdto.RegionsDTO;
import com.kula.kula_project_backend.service.IRegionService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/regions")
public class RegionsController {

    @Autowired
    private IRegionService regionsService;

    /**
     * Endpoint to get all regions.
     * @return The result of the get operation.
     */
    @GetMapping("/all")
    public ResponseResult getAllRegions() {
        return regionsService.getAll();
    }

    /**
     * Endpoint to get single region by id.
     * @return The result of the single region
     */
    @GetMapping("{id}")
    public ResponseResult getRegionById(@PathVariable ObjectId id) {
        return regionsService.getById(id);
    }

    /**
     * Endpoint to save a new region.
     * @param regionDTO The region data transfer object containing the region details.
     * @return The result of the save operation.
     */
    @PostMapping("/save")
    public ResponseResult saveRegion(@RequestBody @Validated(SaveValidator.class) RegionsDTO regionDTO) {
        return regionsService.save(regionDTO);
    }

    /**
     * Endpoint to update an region.
     * @param regionDTO The region data transfer object containing the updated region details.
     * @return The result of the update operation.
     */
    @PutMapping("/update")
    public ResponseResult updateRegion(@RequestBody @Validated RegionsDTO regionDTO) {
        return regionsService.update(regionDTO);
    }

    /**
     * Endpoint to delete region by id.
     * @param id The id of the region.
     * @return The result of deletion.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseResult deleteRegion(@PathVariable ObjectId id) {
        return regionsService.delete(id);
    }
}
