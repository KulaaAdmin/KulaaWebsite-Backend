package com.kula.kula_project_backend.service;

import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.dto.requestdto.RegionsDTO;
import org.bson.types.ObjectId;

public interface IRegionService {
    ResponseResult getAll();
    ResponseResult getById(ObjectId id);
    ResponseResult save(RegionsDTO regionDTO);
    ResponseResult update(RegionsDTO regionDTO);
    ResponseResult delete(ObjectId id);
}
