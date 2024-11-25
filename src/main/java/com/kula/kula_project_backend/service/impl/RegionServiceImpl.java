package com.kula.kula_project_backend.service.impl;

import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.common.converter.RegionsResponseDTOConverter;
import com.kula.kula_project_backend.dao.RegionsRepository;
import com.kula.kula_project_backend.dto.requestdto.RegionsDTO;
import com.kula.kula_project_backend.dto.responsedto.RegionsResponseDTO;
import com.kula.kula_project_backend.entity.Areas;
import com.kula.kula_project_backend.entity.Regions;
import com.kula.kula_project_backend.service.IAzureBlobService;
import com.kula.kula_project_backend.service.IRegionService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RegionServiceImpl implements IRegionService {

    @Autowired
    private RegionsRepository regionsRepository;

    /**
     * Get all regions.
     * @return All regions stored in the system by response result.
     */
    @Override
    public ResponseResult getAll() {
        List<Regions> regions = regionsRepository.findAll();
        List<RegionsResponseDTO> regionList = new ArrayList<>();
        if (!regions.isEmpty()) {
            for (Regions region : regions) {
                RegionsResponseDTO dto = RegionsResponseDTOConverter.convertToResponseDTO(region);
                regionList.add(dto);
            }
            System.out.println(regionList);
            return new ResponseResult(200, "success", regionList);
        }
        return new ResponseResult(400, "fail");
    }

    /**
     * Get a single region by id
     * @param id The id of region object.
     * @return the region object
     */
    @Override
    public ResponseResult getById(ObjectId id) {
        Optional<Regions> region = regionsRepository.findById(id);
        if (region.isPresent()){
            RegionsResponseDTO dto = RegionsResponseDTOConverter.convertToResponseDTO(region.get());
            return new ResponseResult(200, "success", dto);
        }
        return new ResponseResult(400, "fail");
    }

    /**
     * Save a new region.
     * @param regionDTO The regionDTO send by the front end.
     * @return The id of new created region object if success by response result.
     */
    public ResponseResult save(RegionsDTO regionDTO) {
        ResponseResult validationResult = validateRegion(regionDTO,false);
        if (validationResult.getCode()!=200){return validationResult;}
        Regions region = convertToEntity(regionDTO,new Regions());
        regionsRepository.save(region);
        return new ResponseResult(200, region.getId().toString());
    }

    /**
     * Update an existing region.
     * @param regionDTO The regionDTO send by the front end.
     * @return The id of updated region object if success by response result.
     */
    public ResponseResult update(RegionsDTO regionDTO) {
        ResponseResult validationResult = validateRegion(regionDTO,true);
        if (validationResult.getCode()!=200){return validationResult;}
        Optional<Regions> regions = regionsRepository.findById(regionDTO.getId());
        if (regions.isPresent()){
            Regions updatedRegion = convertToEntity(regionDTO, regions.get());
            regionsRepository.save(updatedRegion);
            return new ResponseResult(200, updatedRegion.getId().toString());
        }
        return new ResponseResult(400, "Region not found");
    }

    /**
     * Delete an existing region.
     * @param id The id of object.
     * @return The id of region object if success by response result.
     */
    @Override
    public ResponseResult delete(ObjectId id) {
        regionsRepository.deleteById(id);
        return new ResponseResult(200, "success");
    }

    /**
     * Helper method for validate region
     * @param regionsDTO The regionDTO send by the front end.
     * @param is_update Whether the situation is update or create.
     * @return The validation result.
     */
    private ResponseResult validateRegion(RegionsDTO regionsDTO, Boolean is_update){
        /* Validate frontend should not provide region id when create region */
        if (!is_update && regionsDTO.getId()!=null){return new ResponseResult(400,"ID should be null when create a new region");}
        /* Validate frontend should provide region id when update region */
        if (is_update && regionsDTO.getId()==null){return new ResponseResult(400,"ID should not be null when update an existing region");}
        return new ResponseResult(200,"Region is valid");
    }

    /**
     * Helper method for converting DTO to Entity
     * @param regionDTO The regionDTO send by the front end.
     * @return The updated/created entity.
     */
    private Regions convertToEntity(RegionsDTO regionDTO, Regions region){
        if (regionDTO.getId()!=null) {
            region.setId(regionDTO.getId());
        }
        if (regionDTO.getRegionName()!=null){
            region.setRegionName(regionDTO.getRegionName());
        }
        return region;
    }
}
