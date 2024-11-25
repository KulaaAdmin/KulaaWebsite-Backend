package com.kula.kula_project_backend.service.impl;

import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.common.converter.AreasResponseDTOConverter;
import com.kula.kula_project_backend.dao.AreasRepository;
import com.kula.kula_project_backend.dao.RegionsRepository;
import com.kula.kula_project_backend.dto.requestdto.AreasDTO;
import com.kula.kula_project_backend.dto.responsedto.AreasResponseDTO;
import com.kula.kula_project_backend.entity.Areas;
import com.kula.kula_project_backend.entity.Regions;
import com.kula.kula_project_backend.service.IAreasService;
import com.kula.kula_project_backend.service.IAzureBlobService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AreasServiceImpl implements IAreasService {
    @Autowired
    private AreasRepository areasRepository;
    @Autowired
    private RegionsRepository regionsRepository;
    @Autowired
    private IAzureBlobService azureBlobService;

    /**
     * Get all areas.
     * @return All areas stored in the system by response result.
     */
    @Override
    public ResponseResult getAll() {
        List<Areas> areas = areasRepository.findAll();
        List<AreasResponseDTO> areaList = new ArrayList<>();

        if (!areas.isEmpty()) {
            for (Areas area : areas) {
                AreasResponseDTO dto = AreasResponseDTOConverter.convertToResponseDTO(area);
                areaList.add(dto);
            }
            return new ResponseResult(200, "success", areaList);
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
        Optional<Areas> area = areasRepository.findById(id);
        if (area.isPresent()){
            AreasResponseDTO dto = AreasResponseDTOConverter.convertToResponseDTO(area.get());
            return new ResponseResult(200, "success", dto);
        }
        return new ResponseResult(400, "fail");
    }

    /**
     * Save a new area.
     * @param areaDTO The areaDTO send by the front end.
     * @return The id of new created area object if success by response result.
     */
    public ResponseResult save(AreasDTO areaDTO) {
        ResponseResult validationResult = validateArea(areaDTO,false);
        if (validationResult.getCode()!=200){return validationResult;}
        Areas area = convertToEntity(areaDTO,new Areas());
        areasRepository.save(area);
        return new ResponseResult(200, area.getId().toString());
    }

    /**
     * Update an existing area.
     * @param areaDTO The areaDTO send by the front end.
     * @return The id of updated area object if success by response result.
     */
    public ResponseResult update(AreasDTO areaDTO) {
        ResponseResult validationResult = validateArea(areaDTO,true);
        if (validationResult.getCode()!=200){return validationResult;}
        Optional<Areas> area = areasRepository.findById(areaDTO.getId());
        if (area.isPresent()){
            Areas updatedArea = convertToEntity(areaDTO, area.get());
            areasRepository.save(updatedArea);
            return new ResponseResult(200, updatedArea.getId().toString());
        }
        return new ResponseResult(400, "Area not found");
    }

    /**
     * Delete an existing area.
     * @param id The id of object.
     * @return The id of area object if success by response result.
     */
    @Override
    public ResponseResult delete(ObjectId id) {
        areasRepository.deleteById(id);
        return new ResponseResult(200, "success");
    }

    /**
     * Helper method for validate area
     * @param areaDTO The areaDTO send by the front end.
     * @param is_update Whether the situation is update or create.
     * @return The validation result.
     */
    private ResponseResult validateArea(AreasDTO areaDTO, Boolean is_update){
        /* Validate frontend should not provide region id when create area */
        if (!is_update && areaDTO.getId()!=null){return new ResponseResult(400,"ID should be null when create a new area");}
        /* Validate frontend should provide region id when update area */
        if (is_update && areaDTO.getId()==null){return new ResponseResult(400,"ID should not be null when update an existing area");}
        return new ResponseResult(200,"area is valid");
    }

    /**
     * Helper Function to upload image for area.
     *
     * @param image The image file waiting for upload.
     * @param area The area object to be updated.
     * @return updated area object.
     */
    public Areas uploadImage(MultipartFile image, Areas area) {
        deleteImage(area.getId());
        ResponseResult uploadResult = azureBlobService.uploadFile(image);
        if (uploadResult.getCode() != 200) {return null;}
        String filename = uploadResult.getData().toString();
        area.setImage(filename);
        return area;
    }


    /**
     * Delete image for an area by its ID.
     *
     * @param id The ID of the area.
     */
    public void deleteImage(ObjectId id) {
        if (id != null){
            Optional<Areas> area = areasRepository.findById(id);
            if (area.isPresent()) {
                String filename = area.get().getImage();
                if (filename!=null){
                    area.get().setImage(null);
                    areasRepository.save(area.get());
                    azureBlobService.deleteFileFromAzure(filename);
                }
            }
        }
    }

    /**
     * Helper method for converting DTO to Entity
     * @param areaDTO The areaDTO send by the front end.
     * @param area A new area object or pre-existing area object.
     * @return The updated/created entity.
     */
    private Areas convertToEntity(AreasDTO areaDTO, Areas area){
        if (areaDTO.getId()!=null) {
            area.setId(areaDTO.getId());
        }
        if (areaDTO.getAreaName()!=null){
            area.setAreaName(areaDTO.getAreaName());
        }
        if (areaDTO.getRegion()!=null){
            Optional<Regions> region = regionsRepository.findByRegionName(areaDTO.getRegion());
            if (region.isPresent()){area.setRegion(region.get().getId());}
        }
        if (areaDTO.getContent()!=null){
            area.setContent(areaDTO.getContent());
        }
        if (areaDTO.getImage()!=null){
            area = uploadImage(areaDTO.getImage(),area);
        }
        return area;
    }
}
