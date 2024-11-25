package com.kula.kula_project_backend.controller;

import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.common.validator.SaveValidator;
import com.kula.kula_project_backend.dto.requestdto.AreasDTO;
import com.kula.kula_project_backend.dto.requestdto.RegionsDTO;
import com.kula.kula_project_backend.service.IAreasService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/areas")
public class AreasController {

    @Autowired
    private IAreasService areasService;

    /**
     * Endpoint to get all areas.
     * @return The result of the get operation.
     */
    @GetMapping("/all")
    public ResponseResult getAllAreas() {
        return areasService.getAll();
    }

    /**
     * Endpoint to get single area by id.
     * @return The result of the single area
     */
    @GetMapping("{id}")
    public ResponseResult getAreaById(@PathVariable ObjectId id) {
        return areasService.getById(id);
    }

    /**
     * Endpoint to save a new area.
     * @param areaDTO The area data transfer object containing the area details.
     * @return The result of the save operation.
     */
    @PostMapping("/save")
    public ResponseResult saveArea(@ModelAttribute @Validated(SaveValidator.class) AreasDTO areaDTO) {
        return areasService.save(areaDTO);
    }

    /**
     * Endpoint to update an area.
     * @param areaDTO The region data transfer object containing the updated region details.
     * @return The result of the update operation.
     */
    @PutMapping("/update")
    public ResponseResult updateArea(@ModelAttribute @Validated AreasDTO areaDTO) {
        return areasService.update(areaDTO);
    }

    /**
     * Endpoint to delete area by id.
     * @param id The id of the area.
     * @return The result of deletion.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseResult deleteArea(@PathVariable ObjectId id) {
        return areasService.delete(id);
    }
}
