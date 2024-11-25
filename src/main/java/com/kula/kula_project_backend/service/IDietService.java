package com.kula.kula_project_backend.service;

import com.kula.kula_project_backend.common.ResponseResult;
import com.kula.kula_project_backend.dto.requestdto.DietsDTO;
import com.kula.kula_project_backend.entity.Diets;
import org.bson.types.ObjectId;

import java.util.List;

public interface IDietService {
        List<Diets> getAll();

        ResponseResult save(DietsDTO dietDTO);

        ResponseResult deleteDiet(ObjectId id);
        ResponseResult getDietByName(String dietName); // 新增方法
        ResponseResult findOrCreateDiet(String dietName);
        ResponseResult getLimitedDiets(int limit);
}
