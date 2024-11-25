package com.kula.kula_project_backend.service;

import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.dto.requestdto.AreasDTO;
import org.bson.types.ObjectId;

public interface IAreasService {
    ResponseResult getAll();
    ResponseResult getById(ObjectId id);
    ResponseResult save(AreasDTO areaDTO);
    ResponseResult update(AreasDTO areaDTO);
    ResponseResult delete(ObjectId id);
}
